package com.nexasupplyai.ai.config;
import com.azure.core.credential.TokenRequestContext;import com.azure.identity.*;import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;import org.springframework.http.HttpHeaders;import org.springframework.stereotype.Component;
import org.springframework.web.context.request.*;import java.time.Duration;import java.util.Locale;
@Component public class EntraInterServiceAuthenticationProvider implements InterServiceAuthenticationProvider{
 private final String mode,scope;private final DefaultAzureCredential credential;
 public EntraInterServiceAuthenticationProvider(@Value("${clients.authentication.mode:propagate}")String mode,
  @Value("${clients.authentication.scope:}")String scope){this.mode=mode.toLowerCase(Locale.ROOT);this.scope=scope;credential=new DefaultAzureCredentialBuilder().build();}
 public void apply(HttpHeaders h){switch(mode){case"none"->{}case"propagate"->propagate(h);case"managed-identity"->managed(h);
  default->throw new IllegalStateException("INTER_SERVICE_AUTH_MODE no soportado: "+mode);}}
 private void propagate(HttpHeaders h){if(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes a){
  HttpServletRequest r=a.getRequest();String v=r.getHeader(HttpHeaders.AUTHORIZATION);if(v!=null&&!v.isBlank())h.set(HttpHeaders.AUTHORIZATION,v);}}
 private void managed(HttpHeaders h){if(scope==null||scope.isBlank())throw new IllegalStateException("AZURE_INTER_SERVICE_SCOPE es obligatorio con managed-identity");
  var t=credential.getToken(new TokenRequestContext().addScopes(scope)).block(Duration.ofSeconds(10));
  if(t==null)throw new IllegalStateException("No se obtuvo token Entra para la llamada interservicio");h.setBearerAuth(t.getToken());}}
