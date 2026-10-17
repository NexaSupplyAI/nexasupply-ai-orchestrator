package com.nexasupplyai.ai.client;

import com.nexasupplyai.ai.dto.external.ProductoResponse;
import com.nexasupplyai.ai.dto.external.SaldoInventarioResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;

@Component
public class InventoryServiceClient {

    private final RestClient cliente;

    public InventoryServiceClient(
            RestClient.Builder b,
            @Value("${clients.inventory-service.base-url:http://localhost:8081}") String u
    ) {
        cliente = b.baseUrl(u).build();
    }

    public ProductoResponse obtenerProducto(UUID id) {
        return cliente.get()
                .uri("/api/v1/productos/{id}", id)
                .retrieve()
                .body(ProductoResponse.class);
    }

    public List<SaldoInventarioResponse> obtenerSaldos(UUID idProducto) {
        List<SaldoInventarioResponse> resultado = cliente.get()
                .uri(uri -> uri.path("/api/v1/saldos-inventario").queryParam("idProducto", idProducto).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return resultado == null ? List.of() : resultado;
    }

    public List<SaldoInventarioResponse> obtenerStockBajo() {
        List<SaldoInventarioResponse> resultado = cliente.get().uri("/api/v1/saldos-inventario/stock-bajo")
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return resultado == null ? List.of() : resultado;
    }
}
