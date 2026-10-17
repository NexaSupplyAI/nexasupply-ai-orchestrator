package com.nexasupplyai.ai.dto.external;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
public record PedidoResponse(UUID id,String numeroPedido,String nombreCliente,String estado,String moneda,
        BigDecimal montoTotal,OffsetDateTime solicitadoEn,OffsetDateTime confirmadoEn) { }
