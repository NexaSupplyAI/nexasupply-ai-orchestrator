package com.nexasupplyai.ai.client;

import com.nexasupplyai.ai.dto.external.ProveedorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class SupplierServiceClient {

    private final RestClient cliente;

    public SupplierServiceClient(
            RestClient.Builder b,
            @Value("${clients.supplier-service.base-url:http://localhost:8083}") String u
    ) {
        cliente = b.baseUrl(u).build();
    }

    public ProveedorResponse obtenerProveedor(UUID id) {
        return cliente.get()
                .uri("/api/v1/proveedores/{id}", id)
                .retrieve()
                .body(ProveedorResponse.class);
    }
}
