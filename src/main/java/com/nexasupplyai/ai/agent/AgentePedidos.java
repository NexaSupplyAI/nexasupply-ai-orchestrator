package com.nexasupplyai.ai.agent;

import com.nexasupplyai.ai.client.OrderServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AgentePedidos implements AgenteDominio {
    private final OrderServiceClient orderServiceClient;
    public String nombre() { return "pedidos"; }
    public Map<String, Object> consultar(UUID idRecurso) { return orderServiceClient.obtenerOrden(idRecurso); }
}
