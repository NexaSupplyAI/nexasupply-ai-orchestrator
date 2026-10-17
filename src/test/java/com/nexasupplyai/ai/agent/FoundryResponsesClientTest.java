package com.nexasupplyai.ai.agent;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoundryResponsesClientTest {
    @Test
    void modoLocalEjecutaHerramientaControlada() {
        AgentToolRegistry herramientas = mock(AgentToolRegistry.class);
        when(herramientas.ejecutar("get_low_stock_products", Map.of()))
                .thenReturn(Map.of("productos", List.of(Map.of("sku", "SKU-1"))));
        FoundryResponsesClient cliente = new FoundryResponsesClient(RestClient.builder(), new ObjectMapper(),
                herramientas, false, "", "modelo", 4);

        var resultado = cliente.responder(List.of(), "¿Qué productos tienen stock crítico?");

        assertEquals("local-fallback", resultado.modelo());
        assertFalse(resultado.invocaciones().isEmpty());
        assertTrue(resultado.texto().contains("SKU-1"));
    }
}
