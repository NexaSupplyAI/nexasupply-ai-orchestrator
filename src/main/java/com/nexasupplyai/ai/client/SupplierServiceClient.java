package com.nexasupplyai.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
public class SupplierServiceClient {

    private final RestClient cliente;

    public SupplierServiceClient(
            RestClient.Builder b,
            @Value("${clients.supplier-service.base-url}") String u
    ) {
        cliente = b.baseUrl(u).build();
    }

    public Map obtenerProveedor(UUID id) {
        return cliente.get()
                .uri("/api/v1/proveedores/{id}", id)
                .retrieve()
                .body(Map.class);
    }
}