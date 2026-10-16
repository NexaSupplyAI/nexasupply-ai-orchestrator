package com.nexasupplyai.ai.dto.external;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record SnapshotPanelExternoResponse(
        UUID id,
        OffsetDateTime inicioPeriodo,
        OffsetDateTime finPeriodo,
        Long totalProductos,
        Long pedidosPendientes,
        BigDecimal valorInventario,
        BigDecimal valorPedidos
) {
}
