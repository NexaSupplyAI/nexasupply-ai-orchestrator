package com.nexasupplyai.ai.agent;

import com.nexasupplyai.ai.client.*;
import com.nexasupplyai.ai.dto.external.PedidoResponse;
import com.nexasupplyai.ai.dto.external.ProductoResponse;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgentToolRegistryTest {
    @Test
    void pendingOrdersFiltraEstadosTerminales() {
        InventoryServiceClient inventario = mock(InventoryServiceClient.class);
        OrderServiceClient pedidos = mock(OrderServiceClient.class);
        SupplierServiceClient proveedores = mock(SupplierServiceClient.class);
        ReportingServiceClient reportes = mock(ReportingServiceClient.class);
        PedidoResponse pendiente = pedido("PENDING");
        PedidoResponse completado = pedido("COMPLETED");
        when(pedidos.listarOrdenes()).thenReturn(List.of(pendiente, completado));

        Map<String, Object> resultado = new AgentToolRegistry(inventario, pedidos, proveedores, reportes)
                .ejecutar("get_pending_orders", Map.of());

        assertEquals(List.of(pendiente), resultado.get("pedidos"));
        verifyNoInteractions(inventario, proveedores, reportes);
    }

    @Test
    void rechazaHerramientaNoRegistrada() {
        var registry = new AgentToolRegistry(mock(InventoryServiceClient.class), mock(OrderServiceClient.class),
                mock(SupplierServiceClient.class), mock(ReportingServiceClient.class));
        assertThrows(IllegalArgumentException.class, () -> registry.ejecutar("delete_product", Map.of()));
    }

    @Test
    void stockDeProductoUsaIdProductoExplicito() {
        InventoryServiceClient inventario = mock(InventoryServiceClient.class);
        UUID idProducto = UUID.randomUUID();
        when(inventario.obtenerProducto(idProducto)).thenReturn(mock(ProductoResponse.class));
        when(inventario.obtenerSaldos(idProducto)).thenReturn(List.of());
        AgentToolRegistry registry = new AgentToolRegistry(inventario, mock(OrderServiceClient.class),
                mock(SupplierServiceClient.class), mock(ReportingServiceClient.class));

        registry.ejecutar("get_product_stock", Map.of("idProducto", idProducto.toString()));

        verify(inventario).obtenerProducto(idProducto);
        verify(inventario).obtenerSaldos(idProducto);
    }

    private PedidoResponse pedido(String estado) {
        return new PedidoResponse(UUID.randomUUID(), "ORD-1", "Cliente", estado, "PEN",
                BigDecimal.TEN, null, null);
    }
}
