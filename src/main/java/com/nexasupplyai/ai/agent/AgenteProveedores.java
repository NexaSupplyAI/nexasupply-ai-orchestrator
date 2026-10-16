package com.nexasupplyai.ai.agent;

import com.nexasupplyai.ai.client.SupplierServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AgenteProveedores implements AgenteDominio {
    private final SupplierServiceClient supplierServiceClient;
    public String nombre() { return "proveedores"; }
    public Map<String, Object> consultar(UUID idRecurso) { return supplierServiceClient.obtenerProveedor(idRecurso); }
}
