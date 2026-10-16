package com.nexasupplyai.ai.tool;
import com.nexasupplyai.ai.agent.AgenteProveedores; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Component; import java.util.*;
@Component @RequiredArgsConstructor public class ConsultarProveedorTool implements HerramientaDominio {private final AgenteProveedores agente; public String nombre(){return "consultar_proveedor";} public Map<String,Object> ejecutar(UUID id){return agente.consultar(id);}}
