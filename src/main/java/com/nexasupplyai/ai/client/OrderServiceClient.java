package com.nexasupplyai.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
public class OrderServiceClient {

    private final RestClient cliente;

    public OrderServiceClient(
            RestClient.Builder b,
            @Value("${clients.order-service.base-url}") String u
    ) {
        cliente = b.baseUrl(u).build();
    }

    public Map obtenerOrden(UUID id) {
        return cliente.get()
                .uri("/api/v1/ordenes/{id}", id)
                .retrieve()
                .body(Map.class);
    }
}