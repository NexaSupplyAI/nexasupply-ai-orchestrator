package com.nexasupplyai.ai.tool;
import com.nexasupplyai.ai.agent.AgentePedidos; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Component; import java.util.*;
@Component @RequiredArgsConstructor public class ConsultarPedidoTool implements HerramientaDominio {private final AgentePedidos agente; public String nombre(){return "consultar_pedido";} public Map<String,Object> ejecutar(UUID id){return agente.consultar(id);}}
