package com.nexasupplyai.ai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ConversacionNoEncontradaException.class,
            MensajeNoEncontradoException.class,
            EjecucionHerramientaNoEncontradaException.class,
            EventoAuditoriaNoEncontradoException.class
    })
    ProblemDetail noEncontrado(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }
}
