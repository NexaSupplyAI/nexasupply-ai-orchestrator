package com.nexasupplyai.ai.dto.request;

import com.nexasupplyai.ai.enums.EstadoEjecucionHerramienta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.UUID;

public record EjecucionHerramientaRequest(

        @NotNull
        UUID idConversacion,

        UUID idMensaje,

        @NotBlank
        @Size(max = 120)
        String nombreHerramienta,

        Map<String, Object> solicitud,

        Map<String, Object> respuesta,

        @NotNull
        EstadoEjecucionHerramienta estado,

        @PositiveOrZero
        Integer duracionMs,

        String mensajeError

) {
}