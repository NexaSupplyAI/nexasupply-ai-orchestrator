package com.nexasupplyai.ai.tool;

import java.util.Map;
import java.util.UUID;

public interface HerramientaDominio {
    String nombre();
    Map<String, Object> ejecutar(UUID idRecurso);
}
