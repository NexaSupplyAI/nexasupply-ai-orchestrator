package com.nexasupplyai.ai.tool;
import com.nexasupplyai.ai.agent.AgenteReportes; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Component; import java.util.*;
@Component @RequiredArgsConstructor public class ConsultarReporteTool implements HerramientaDominio {private final AgenteReportes agente; public String nombre(){return "consultar_reporte";} public Map<String,Object> ejecutar(UUID id){return agente.consultar(id);}}
