package com.nexasupplyai.ai.dto.external;
import java.math.BigDecimal;
import java.util.UUID;
public record ProductoResponse(UUID id,String sku,String nombre,String descripcion,String unidadMedida,
        BigDecimal precioVenta,BigDecimal stockMinimo,BigDecimal puntoReorden,BigDecimal cantidadReorden,Boolean activo) { }
