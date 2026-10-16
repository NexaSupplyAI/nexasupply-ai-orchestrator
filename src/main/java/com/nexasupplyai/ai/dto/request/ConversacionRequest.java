package com.nexasupplyai.ai.dto.request;

import com.nexasupplyai.ai.enums.EstadoConversacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record ConversacionRequest(

        @NotBlank
        @Size(max = 120)
        String idUsuarioObjeto,

        @Size(max = 220)
        String titulo,

        EstadoConversacion estado,

        OffsetDateTime cerradaEn

) {
}