package com.nexasupplyai.ai.dto.request;

import com.nexasupplyai.ai.enums.RolMensaje;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MensajeRequest(

        @NotNull
        UUID idConversacion,

        @NotNull
        RolMensaje rol,

        @NotBlank
        String contenido,

        @Size(max = 120)
        String nombreModelo,

        @NotNull
        @PositiveOrZero
        Integer tokensEntrada,

        @NotNull
        @PositiveOrZero
        Integer tokensSalida,

        UUID idCorrelacion

) {
}