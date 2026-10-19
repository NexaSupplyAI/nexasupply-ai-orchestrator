package com.nexasupplyai.ai.agent;

import com.nexasupplyai.ai.client.InventoryServiceClient;
import com.nexasupplyai.ai.client.OrderServiceClient;
import com.nexasupplyai.ai.client.ReportingServiceClient;
import com.nexasupplyai.ai.client.SupplierServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AgentToolRegistry {
    private final InventoryServiceClient inventario;
    private final OrderServiceClient pedidos;
    private final SupplierServiceClient proveedores;
    private final ReportingServiceClient reportes;

    public List<Map<String, Object>> definiciones() {
        return List.of(
                funcionConId("get_product_stock",
                        "Consulta un producto y sus saldos. Usa el campo idProducto de los saldos, nunca el campo id del saldo.",
                        "idProducto", "UUID del producto; corresponde exactamente al campo idProducto"),
                funcion("get_low_stock_products", "Lista saldos con stock bajo; cada resultado distingue id del saldo e idProducto", false),
                funcion("get_pending_orders", "Lista pedidos pendientes, confirmados o en proceso", false),
                funcionConId("get_supplier_information", "Consulta un proveedor por UUID",
                        "idProveedor", "UUID del proveedor"),
                funcion("get_dashboard", "Genera y devuelve el dashboard operativo actual", false)
        );
    }

    public Map<String, Object> ejecutar(String nombre, Map<String, Object> argumentos) {
        return switch (nombre) {
            case "get_product_stock" -> Map.of(
                    "producto", inventario.obtenerProducto(uuid(argumentos, "idProducto")),
                    "saldos", inventario.obtenerSaldos(uuid(argumentos, "idProducto")));
            case "get_low_stock_products" -> Map.of("productos", inventario.obtenerStockBajo());
            case "get_pending_orders" -> Map.of("pedidos", pedidos.listarOrdenes().stream()
                    .filter(p -> Set.of("PENDING", "CONFIRMED", "PROCESSING")
                            .contains(p.estado())).toList());
            case "get_supplier_information" -> Map.of("proveedor", proveedores.obtenerProveedor(uuid(argumentos, "idProveedor")));
            case "get_dashboard" -> Map.of("dashboard", reportes.obtenerDashboard());
            default -> throw new IllegalArgumentException("Herramienta no autorizada: " + nombre);
        };
    }

    private UUID uuid(Map<String, Object> argumentos, String nombre) {
        Object valor = argumentos.get(nombre);
        if (valor == null) throw new IllegalArgumentException("La herramienta requiere el argumento " + nombre);
        return UUID.fromString(String.valueOf(valor));
    }

    private Map<String, Object> funcionConId(String nombre, String descripcion, String nombreId,
                                              String descripcionId) {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("type", "object");
        parametros.put("properties", Map.of(nombreId,
                Map.of("type", "string", "format", "uuid", "description", descripcionId)));
        parametros.put("required", List.of(nombreId));
        parametros.put("additionalProperties", false);
        return Map.of("type", "function", "name", nombre, "description", descripcion,
                "parameters", parametros, "strict", true);
    }

    private Map<String, Object> funcion(String nombre, String descripcion, boolean requiereId) {
        Map<String, Object> propiedades = requiereId
                ? Map.of("id", Map.of("type", "string", "format", "uuid", "description", "UUID del recurso"))
                : Map.of();
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("type", "object");
        parametros.put("properties", propiedades);
        parametros.put("required", requiereId ? List.of("id") : List.of());
        parametros.put("additionalProperties", false);
        return Map.of("type", "function", "name", nombre, "description", descripcion,
                "parameters", parametros, "strict", true);
    }
}
