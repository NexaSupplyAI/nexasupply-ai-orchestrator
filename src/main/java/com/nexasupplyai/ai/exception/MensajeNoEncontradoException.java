package com.nexasupplyai.ai.exception;

import java.util.UUID;

public class MensajeNoEncontradoException extends RuntimeException {

    public MensajeNoEncontradoException(UUID id) {
        super("Mensaje no encontrado: " + id);
    }
}