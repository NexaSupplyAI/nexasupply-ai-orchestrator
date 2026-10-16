package com.nexasupplyai.ai.tool;

import com.nexasupplyai.ai.agent.AgenteInventario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConsultarInventarioTool implements HerramientaDominio {
    private final AgenteInventario agente;
    public String nombre() { return "consultar_inventario"; }
    public Map<String, Object> ejecutar(UUID idRecurso) { return agente.consultar(idRecurso); }
}
