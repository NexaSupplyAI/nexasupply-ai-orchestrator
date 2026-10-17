package com.nexasupplyai.ai.client;

import com.nexasupplyai.ai.dto.external.PedidoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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

    public PedidoResponse obtenerOrden(UUID id) {
        return cliente.get()
                .uri("/api/v1/ordenes/{id}", id)
                .retrieve()
                .body(PedidoResponse.class);
    }

    public List<PedidoResponse> listarOrdenes() {
        List<PedidoResponse> resultado = cliente.get().uri("/api/v1/ordenes").retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return resultado == null ? List.of() : resultado;
    }
}
