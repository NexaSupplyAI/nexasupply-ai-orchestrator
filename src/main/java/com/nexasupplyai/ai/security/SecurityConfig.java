package com.nexasupplyai.ai.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

 @Bean
 @ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = true)
 public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity http) throws Exception {

  return http
          .csrf(csrf -> csrf.disable())

          .cors(Customizer.withDefaults())

          .sessionManagement(s ->
                  s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )

          .authorizeHttpRequests(a -> a
                  .requestMatchers(
                          "/actuator/health",
                          "/actuator/health/**"
                  ).permitAll()

                  .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("NEXASTOCK_ADMIN", "NEXASTOCK_OPERATOR", "NEXASTOCK_AUDITOR")
                  .requestMatchers("/api/v1/chat").hasAnyRole("NEXASTOCK_ADMIN", "NEXASTOCK_OPERATOR", "NEXASTOCK_AUDITOR")
                  .requestMatchers("/api/**").hasAnyRole("NEXASTOCK_ADMIN", "NEXASTOCK_OPERATOR")

                  .anyRequest().denyAll()
          )

          .oauth2ResourceServer(o -> o
                  .authenticationEntryPoint(
                          new BearerTokenAuthenticationEntryPoint()
                  )
                  .accessDeniedHandler(
                          new BearerTokenAccessDeniedHandler()
                  )
                  .jwt(jwt -> jwt.jwtAuthenticationConverter(conversorJwt()))
          )

          .build();
 }

 @Bean
 @ConditionalOnProperty(name = "app.security.enabled", havingValue = "false")
 SecurityFilterChain cadenaFiltrosLocal(HttpSecurity http) throws Exception {
  return http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
          .authorizeHttpRequests(a -> a.anyRequest().permitAll()).build();
 }

 @Bean
 JwtAuthenticationConverter conversorJwt() {
  JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();
  conversor.setJwtGrantedAuthoritiesConverter(jwt -> {
   Object claim = jwt.getClaims().get("roles");
   if (!(claim instanceof java.util.Collection<?> roles)) return java.util.List.of();
   return roles.stream().map(String::valueOf)
           .map(rol -> "ROLE_" + rol.toUpperCase(java.util.Locale.ROOT).replaceAll("[^A-Z0-9]", "_"))
           .map(rol -> (GrantedAuthority) new SimpleGrantedAuthority(rol)).toList();
  });
  return conversor;
 }
}
