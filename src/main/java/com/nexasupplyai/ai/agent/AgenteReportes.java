package com.nexasupplyai.ai.agent;

import com.nexasupplyai.ai.client.ReportingServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AgenteReportes implements AgenteDominio {
    private final ReportingServiceClient reportingServiceClient;
    public String nombre() { return "reportes"; }
    public Map<String, Object> consultar(UUID idRecurso) { return reportingServiceClient.obtenerSnapshot(idRecurso); }
}
