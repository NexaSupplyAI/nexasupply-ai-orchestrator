package com.nexasupplyai.ai.dto.external;
import java.math.BigDecimal;
import java.util.UUID;
public record ProveedorResponse(UUID id,String codigoProveedor,String numeroFiscal,String razonSocial,
        String nombreComercial,String email,String telefono,String estado,BigDecimal calificacion,
        Integer diasEntregaPredeterminados) { }
