package com.nexasupplyai.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
public class ReportingServiceClient {

    private final RestClient cliente;

    public ReportingServiceClient(
            RestClient.Builder b,
            @Value("${clients.reporting-service.base-url}") String u
    ) {
        cliente = b.baseUrl(u).build();
    }

    public Map obtenerSnapshot(UUID id) {
        return cliente.get()
                .uri("/api/v1/snapshots-panel/{id}", id)
                .retrieve()
                .body(Map.class);
    }
}