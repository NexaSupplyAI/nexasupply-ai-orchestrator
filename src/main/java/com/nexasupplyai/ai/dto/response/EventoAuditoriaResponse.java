package com.nexasupplyai.ai.dto.response;

import com.nexasupplyai.ai.enums.DecisionAuditoria;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record EventoAuditoriaResponse(

        UUID id,
        UUID idConversacion,
        String idUsuarioObjeto,
        String accion,
        String tipoRecurso,
        UUID idRecurso,
        DecisionAuditoria decision,
        String motivo,
        Map<String, Object> metadatos,
        OffsetDateTime creadoEn

) {
}