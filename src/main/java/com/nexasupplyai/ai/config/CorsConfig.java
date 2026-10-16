package com.nexasupplyai.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

 private final String[] origenes;

 public CorsConfig(
         @Value("${app.cors.allowed-origins:http://localhost:3000}") String valor
 ) {
  origenes = Arrays.stream(valor.split(","))
          .map(String::trim)
          .filter(v -> !v.isEmpty())
          .toArray(String[]::new);
 }

 @Override
 public void addCorsMappings(CorsRegistry r) {
  r.addMapping("/api/**")
          .allowedOrigins(origenes)
          .allowedMethods(
                  "GET",
                  "POST",
                  "PUT",
                  "PATCH",
                  "DELETE",
                  "OPTIONS"
          )
          .allowedHeaders("*")
          .allowCredentials(true)
          .maxAge(3600);
 }
}