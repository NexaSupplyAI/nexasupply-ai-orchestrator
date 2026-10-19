package com.nexasupplyai.ai.agent;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoundryResponsesClientTest {
    @Test
    void modoLocalDevuelveErrorControladoSinEjecutarHerramientas() {
        AgentToolRegistry herramientas = mock(AgentToolRegistry.class);
        FoundryResponsesClient cliente = new FoundryResponsesClient(new ObjectMapper(),
                herramientas, false, "", "modelo", "https://cognitiveservices.azure.com/.default", 3, 800, 12, 24000,
                java.time.Duration.ofSeconds(1), java.time.Duration.ofSeconds(1));

        assertThrows(com.nexasupplyai.ai.exception.FoundryUnavailableException.class,
                () -> cliente.responder(List.of()));

        verifyNoInteractions(herramientas);
    }

    @Test
    void limitaHistorialPorCantidadYCaracteresConservandoLoMasReciente() {
        FoundryResponsesClient cliente = new FoundryResponsesClient(new ObjectMapper(),
                mock(AgentToolRegistry.class), false, "", "modelo", "https://cognitiveservices.azure.com/.default", 3, 800, 3, 9,
                java.time.Duration.ofSeconds(1), java.time.Duration.ofSeconds(1));
        List<Map<String, Object>> historial = List.of(
                Map.of("role", "user", "content", "antiguo"),
                Map.of("role", "assistant", "content", "12345"),
                Map.of("role", "user", "content", "6789"),
                Map.of("role", "assistant", "content", "fin"));

        List<Map<String, Object>> limitado = cliente.limitarHistorial(historial);

        assertEquals(List.of(historial.get(2), historial.get(3)), limitado);
    }
}
