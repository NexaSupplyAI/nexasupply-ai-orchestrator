package com.nexasupplyai.ai.dto.response;

import com.nexasupplyai.ai.enums.RolMensaje;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MensajeResponse(

        UUID id,
        UUID idConversacion,
        RolMensaje rol,
        String contenido,
        String nombreModelo,
        Integer tokensEntrada,
        Integer tokensSalida,
        UUID idCorrelacion,
        OffsetDateTime creadoEn

) {
}