package com.nexasupplyai.ai.exception;
import com.nexasupplyai.ai.config.CorrelationIdFilter;import jakarta.servlet.http.HttpServletRequest;import org.slf4j.*;
import org.springframework.http.*;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;import java.time.*;import java.util.*;import java.util.stream.Collectors;
@RestControllerAdvice public class GlobalExceptionHandler{
 private static final Logger log=LoggerFactory.getLogger(GlobalExceptionHandler.class);
 @ExceptionHandler({ConversacionNoEncontradaException.class,MensajeNoEncontradoException.class,EjecucionHerramientaNoEncontradaException.class,EventoAuditoriaNoEncontradoException.class})
 ResponseEntity<ApiError> notFound(RuntimeException e,HttpServletRequest r){return out(HttpStatus.NOT_FOUND,"RESOURCE_NOT_FOUND",e.getMessage(),r,Map.of());}
 @ExceptionHandler(FoundryUnavailableException.class) ResponseEntity<ApiError> foundry(FoundryUnavailableException e,HttpServletRequest r){
  return out(HttpStatus.SERVICE_UNAVAILABLE,"FOUNDRY_NOT_CONFIGURED",e.getMessage(),r,Map.of());}
 @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class}) ResponseEntity<ApiError> invalid(RuntimeException e,HttpServletRequest r){
  return out(HttpStatus.BAD_REQUEST,"INVALID_REQUEST",e.getMessage(),r,Map.of());}
 @ExceptionHandler(SecurityException.class) ResponseEntity<ApiError> forbidden(SecurityException e,HttpServletRequest r){
  return out(HttpStatus.FORBIDDEN,"ACCESS_DENIED",e.getMessage(),r,Map.of());}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiError> validation(MethodArgumentNotValidException e,HttpServletRequest r){
  var f=e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(x->x.getField(),x->x.getDefaultMessage()==null?"Valor inválido":x.getDefaultMessage(),(a,b)->a));
  return out(HttpStatus.BAD_REQUEST,"VALIDATION_ERROR","La solicitud contiene campos inválidos",r,f);}
 @ExceptionHandler(RestClientException.class) ResponseEntity<ApiError> dependency(RestClientException e,HttpServletRequest r){log.warn("Dependencia no disponible: {}",e.getMessage());
  return out(HttpStatus.BAD_GATEWAY,"DEPENDENCY_UNAVAILABLE","No fue posible consultar una dependencia del agente",r,Map.of());}
 @ExceptionHandler(Exception.class) ResponseEntity<ApiError> unexpected(Exception e,HttpServletRequest r){log.error("Error inesperado",e);
  return out(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_ERROR","Ocurrió un error interno en el agente",r,Map.of());}
 private ResponseEntity<ApiError> out(HttpStatus s,String c,String m,HttpServletRequest r,Map<String,String> f){return ResponseEntity.status(s)
  .body(new ApiError(s.value(),c,m,r.getRequestURI(),MDC.get(CorrelationIdFilter.MDC_KEY),OffsetDateTime.now(ZoneOffset.UTC),f));}
}
