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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

 @Bean
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

                  .requestMatchers("/api/**").authenticated()

                  .anyRequest().denyAll()
          )

          .oauth2ResourceServer(o -> o
                  .authenticationEntryPoint(
                          new BearerTokenAuthenticationEntryPoint()
                  )
                  .accessDeniedHandler(
                          new BearerTokenAccessDeniedHandler()
                  )
                  .jwt(Customizer.withDefaults())
          )

          .build();
 }
}