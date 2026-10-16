package com.nexasupplyai.ai.exception;

import java.util.UUID;

public class EjecucionHerramientaNoEncontradaException extends RuntimeException {

    public EjecucionHerramientaNoEncontradaException(UUID id) {
        super("Ejecución no encontrada: " + id);
    }
}
