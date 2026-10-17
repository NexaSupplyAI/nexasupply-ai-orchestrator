package com.nexasupplyai.ai.security;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;import org.springframework.security.web.SecurityFilterChain;
import java.util.ArrayList;import java.util.Locale;
@Configuration @EnableWebSecurity @EnableMethodSecurity public class SecurityConfig{
 private static final String[] DOCS={"/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html"};
 @Bean @ConditionalOnProperty(name="app.security.enabled",havingValue="true") SecurityFilterChain secured(HttpSecurity h)throws Exception{return h
  .csrf(c->c.disable()).cors(Customizer.withDefaults()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
  .authorizeHttpRequests(a->a.requestMatchers("/actuator/health","/actuator/health/**").permitAll().requestMatchers(DOCS).hasRole(AppRoles.ADMIN)
   .requestMatchers("/api/**").hasAnyRole(AppRoles.ADMIN,AppRoles.AI_USER).anyRequest().denyAll())
  .oauth2ResourceServer(o->o.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
   .accessDeniedHandler(new BearerTokenAccessDeniedHandler()).jwt(j->j.jwtAuthenticationConverter(converter()))).build();}
 @Bean @ConditionalOnProperty(name="app.security.enabled",havingValue="false",matchIfMissing=true) SecurityFilterChain local(HttpSecurity h)throws Exception{return h
  .csrf(c->c.disable()).cors(Customizer.withDefaults()).authorizeHttpRequests(a->a.anyRequest().permitAll()).build();}
 @Bean JwtAuthenticationConverter converter(){var c=new JwtAuthenticationConverter();var scopesConverter=new JwtGrantedAuthoritiesConverter();
  c.setJwtGrantedAuthoritiesConverter(jwt->{var authorities=new ArrayList<GrantedAuthority>();var scopes=scopesConverter.convert(jwt);
   if(scopes!=null)authorities.addAll(scopes);Object claim=jwt.getClaims().get("roles");
   if(claim instanceof java.util.Collection<?> roles)roles.stream().map(String::valueOf)
    .map(r->"ROLE_"+r.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]","_")).map(SimpleGrantedAuthority::new)
    .forEach(authorities::add);return authorities;});return c;}
}
