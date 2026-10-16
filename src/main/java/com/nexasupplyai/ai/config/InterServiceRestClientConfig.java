package com.nexasupplyai.ai.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class InterServiceRestClientConfig {

    @Bean
    RestClientCustomizer propagarCabecerasDeSolicitud() {
        return builder -> builder.requestInterceptor((request, body, execution) -> {
            if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes atributos) {
                HttpServletRequest solicitud = atributos.getRequest();
                copiarSiExiste(solicitud, request.getHeaders(), HttpHeaders.AUTHORIZATION);
                copiarSiExiste(solicitud, request.getHeaders(), "X-Correlation-ID");
            }
            return execution.execute(request, body);
        });
    }

    private void copiarSiExiste(HttpServletRequest origen, HttpHeaders destino, String nombre) {
        String valor = origen.getHeader(nombre);
        if (valor != null && !valor.isBlank()) {
            destino.set(nombre, valor);
        }
    }
}
