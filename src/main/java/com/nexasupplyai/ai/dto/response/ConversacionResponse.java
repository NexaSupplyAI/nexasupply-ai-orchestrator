package com.nexasupplyai.ai.dto.response;

import com.nexasupplyai.ai.enums.EstadoConversacion;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ConversacionResponse(

        UUID id,
        String idUsuarioObjeto,
        String titulo,
        EstadoConversacion estado,
        OffsetDateTime iniciadaEn,
        OffsetDateTime ultimaActividadEn,
        OffsetDateTime cerradaEn

) {
}