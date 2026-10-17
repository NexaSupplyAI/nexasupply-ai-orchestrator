package com.nexasupplyai.ai.client;

import com.nexasupplyai.ai.dto.external.DashboardResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class ReportingServiceClient {

    private final RestClient cliente;

    public ReportingServiceClient(
            RestClient.Builder b,
            @Value("${clients.reporting-service.base-url:http://localhost:8084}") String u
    ) {
        cliente = b.baseUrl(u).build();
    }

    public DashboardResponse obtenerDashboard() {
        return cliente.get().uri("/api/v1/reportes/dashboard").retrieve().body(DashboardResponse.class);
    }
}
