package com.nexasupplyai.ai.agent;

import java.util.Map;
import java.util.UUID;

public interface AgenteDominio {
    String nombre();
    Map<String, Object> consultar(UUID idRecurso);
}
