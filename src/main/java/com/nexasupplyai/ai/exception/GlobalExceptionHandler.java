package com.nexasupplyai.ai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestClientException;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler({IllegalArgumentException.class})
    ProblemDetail solicitudInvalida(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    ProblemDetail prohibido(SecurityException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacion(MethodArgumentNotValidException e) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "La solicitud contiene campos inválidos");
        Map<String, String> errores = e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                error -> error.getField(), error -> error.getDefaultMessage() == null ? "Valor inválido" : error.getDefaultMessage(),
                (primero, segundo) -> primero));
        problema.setProperty("errores", errores);
        return problema;
    }

    @ExceptionHandler(RestClientException.class)
    ProblemDetail dependencia(RestClientException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, "No fue posible consultar una dependencia del agente");
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail inesperado(Exception e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno en el agente");
    }
}
