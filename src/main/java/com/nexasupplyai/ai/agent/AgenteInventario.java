package com.nexasupplyai.ai.agent;

import com.nexasupplyai.ai.client.InventoryServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AgenteInventario implements AgenteDominio {
    private final InventoryServiceClient inventoryServiceClient;
    public String nombre() { return "inventario"; }
    public Map<String, Object> consultar(UUID idRecurso) { return inventoryServiceClient.obtenerProducto(idRecurso); }
}
