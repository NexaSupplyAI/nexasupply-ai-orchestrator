package com.nexasupplyai.ai.exception;

import java.util.UUID;

public class ConversacionNoEncontradaException extends RuntimeException {

    public ConversacionNoEncontradaException(UUID id) {
        super("Conversación no encontrada: " + id);
    }
}