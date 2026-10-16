package com.nexasupplyai.ai.dto.external;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductoInventarioExternoResponse(
        UUID id,
        String sku,
        String nombre,
        BigDecimal precioVenta,
        Boolean activo
) {
}
