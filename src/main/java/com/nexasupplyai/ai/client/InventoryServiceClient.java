package com.nexasupplyai.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
public class InventoryServiceClient {

    private final RestClient cliente;

    public InventoryServiceClient(
            RestClient.Builder b,
            @Value("${clients.inventory-service.base-url:http://localhost:8081}") String u
    ) {
        cliente = b.baseUrl(u).build();
    }

    public Map obtenerProducto(UUID id) {
        return cliente.get()
                .uri("/api/v1/productos/{id}", id)
                .retrieve()
                .body(Map.class);
    }
}
