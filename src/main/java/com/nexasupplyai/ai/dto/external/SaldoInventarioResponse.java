package com.nexasupplyai.ai.dto.external;
import java.math.BigDecimal;
import java.util.UUID;
public record SaldoInventarioResponse(UUID id,UUID idAlmacen,UUID idProducto,BigDecimal cantidadEnMano,
        BigDecimal cantidadReservada,BigDecimal cantidadDisponible) { }
