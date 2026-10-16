package com.nexasupplyai.ai.dto.request;

import com.nexasupplyai.ai.enums.DecisionAuditoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.UUID;

public record EventoAuditoriaRequest(

        UUID idConversacion,

        @NotBlank
        @Size(max = 120)
        String idUsuarioObjeto,

        @NotBlank
        @Size(max = 120)
        String accion,

        @Size(max = 80)
        String tipoRecurso,

        UUID idRecurso,

        @NotNull
        DecisionAuditoria decision,

        String motivo,

        Map<String, Object> metadatos

) {
}