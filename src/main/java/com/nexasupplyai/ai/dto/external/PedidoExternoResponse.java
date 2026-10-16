package com.nexasupplyai.ai.dto.external;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoExternoResponse(
        UUID id,
        String numeroPedido,
        String estado,
        BigDecimal montoTotal
) {
}
