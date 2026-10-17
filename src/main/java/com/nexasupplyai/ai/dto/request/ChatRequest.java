package com.nexasupplyai.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChatRequest(
        UUID idConversacion,
        @NotBlank @Size(max = 8000) String mensaje
) {
}
