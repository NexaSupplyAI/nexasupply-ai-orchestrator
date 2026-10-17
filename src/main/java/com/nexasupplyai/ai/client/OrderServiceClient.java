package com.nexasupplyai.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;

@Component
public class OrderServiceClient {

    private final RestClient cliente;

    public OrderServiceClient(
            RestClient.Builder b,
            @Value("${clients.order-service.base-url:http://localhost:8082}") String u
    ) {
        cliente = b.baseUrl(u).build();
    }

    public Map obtenerOrden(UUID id) {
        return cliente.get()
                .uri("/api/v1/ordenes/{id}", id)
                .retrieve()
                .body(Map.class);
    }

    public List<Map<String, Object>> listarOrdenes() {
        List<Map<String, Object>> resultado = cliente.get().uri("/api/v1/ordenes").retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return resultado == null ? List.of() : resultado;
    }
}
