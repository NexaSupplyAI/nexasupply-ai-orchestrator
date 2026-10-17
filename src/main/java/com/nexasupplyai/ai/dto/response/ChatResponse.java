package com.nexasupplyai.ai.dto.response;

import java.util.List;
import java.util.UUID;

public record ChatResponse(
        UUID idConversacion,
        UUID idCorrelacion,
        MensajeResponse mensajeUsuario,
        MensajeResponse mensajeAsistente,
        List<EjecucionHerramientaResponse> herramientas
) {
}
