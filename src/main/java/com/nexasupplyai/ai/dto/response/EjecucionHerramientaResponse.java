package com.nexasupplyai.ai.dto.response;

import com.nexasupplyai.ai.enums.EstadoEjecucionHerramienta;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record EjecucionHerramientaResponse(

        UUID id,
        UUID idConversacion,
        UUID idMensaje,
        String nombreHerramienta,
        Map<String, Object> solicitud,
        Map<String, Object> respuesta,
        EstadoEjecucionHerramienta estado,
        Integer duracionMs,
        String mensajeError,
        OffsetDateTime creadoEn

) {
}