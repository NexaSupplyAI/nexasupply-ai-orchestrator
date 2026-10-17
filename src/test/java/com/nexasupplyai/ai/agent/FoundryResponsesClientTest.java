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
                herramientas, false, "", "modelo", 4,
                java.time.Duration.ofSeconds(1), java.time.Duration.ofSeconds(1));

        assertThrows(com.nexasupplyai.ai.exception.FoundryUnavailableException.class,
                () -> cliente.responder(List.of(), "¿Qué productos tienen stock crítico?"));

        verifyNoInteractions(herramientas);
    }
}
