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
                funcion("get_product_stock", "Consulta el stock de un producto por UUID", true),
                funcion("get_low_stock_products", "Lista los productos con stock bajo", false),
                funcion("get_pending_orders", "Lista pedidos pendientes, confirmados o en proceso", false),
                funcion("get_supplier_information", "Consulta un proveedor por UUID", true),
                funcion("get_dashboard", "Genera y devuelve el dashboard operativo actual", false)
        );
    }

    public Map<String, Object> ejecutar(String nombre, Map<String, Object> argumentos) {
        return switch (nombre) {
            case "get_product_stock" -> Map.of(
                    "producto", inventario.obtenerProducto(uuid(argumentos)),
                    "saldos", inventario.obtenerSaldos(uuid(argumentos)));
            case "get_low_stock_products" -> Map.of("productos", inventario.obtenerStockBajo());
            case "get_pending_orders" -> Map.of("pedidos", pedidos.listarOrdenes().stream()
                    .filter(p -> Set.of("PENDING", "CONFIRMED", "PROCESSING")
                            .contains(p.estado())).toList());
            case "get_supplier_information" -> Map.of("proveedor", proveedores.obtenerProveedor(uuid(argumentos)));
            case "get_dashboard" -> Map.of("dashboard", reportes.obtenerDashboard());
            default -> throw new IllegalArgumentException("Herramienta no autorizada: " + nombre);
        };
    }

    private UUID uuid(Map<String, Object> argumentos) {
        Object valor = argumentos.get("id");
        if (valor == null) throw new IllegalArgumentException("La herramienta requiere el argumento id");
        return UUID.fromString(String.valueOf(valor));
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
