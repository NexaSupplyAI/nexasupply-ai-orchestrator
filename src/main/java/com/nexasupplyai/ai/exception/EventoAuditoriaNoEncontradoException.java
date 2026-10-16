package com.nexasupplyai.ai.exception;

import java.util.UUID;

public class EventoAuditoriaNoEncontradoException extends RuntimeException {

    public EventoAuditoriaNoEncontradoException(UUID id) {
        super("Evento no encontrado: " + id);
    }
}