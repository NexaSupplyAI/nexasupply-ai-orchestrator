package com.nexasupplyai.ai.service.impl;

import com.nexasupplyai.ai.agent.FoundryResponsesClient;
import com.nexasupplyai.ai.dto.request.ChatRequest;
import com.nexasupplyai.ai.dto.response.ChatResponse;
import com.nexasupplyai.ai.entity.*;
import com.nexasupplyai.ai.enums.*;
import com.nexasupplyai.ai.exception.ConversacionNoEncontradaException;
import com.nexasupplyai.ai.mapper.EjecucionHerramientaMapper;
import com.nexasupplyai.ai.mapper.MensajeMapper;
import com.nexasupplyai.ai.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ConversacionRepository conversaciones;
    private final MensajeRepository mensajes;
    private final EjecucionHerramientaRepository ejecuciones;
    private final EventoAuditoriaRepository auditorias;
    private final FoundryResponsesClient foundry;
    private final MensajeMapper mensajeMapper;
    private final EjecucionHerramientaMapper ejecucionMapper;

    public ChatResponse conversar(ChatRequest request, String idUsuario) {
        Conversacion conversacion = obtenerOCrearConversacion(request, idUsuario);
        UUID correlacion = UUID.randomUUID();
        Mensaje usuario = guardarMensaje(conversacion, RolMensaje.USER, request.mensaje(), null, 0, 0, correlacion);
        List<Map<String, Object>> historial = mensajes.findAllByConversacionIdOrderByCreadoEnAsc(conversacion.getId())
                .stream().filter(m -> m.getRol() == RolMensaje.USER || m.getRol() == RolMensaje.ASSISTANT)
                .map(m -> Map.<String, Object>of("role", m.getRol() == RolMensaje.USER ? "user" : "assistant",
                        "content", m.getContenido())).toList();
        try {
            FoundryResponsesClient.Resultado resultado = foundry.responder(historial);
            Mensaje asistente = guardarMensaje(conversacion, RolMensaje.ASSISTANT, resultado.texto(),
                    resultado.modelo(), resultado.tokensEntrada(), resultado.tokensSalida(), correlacion);
            List<EjecucionHerramienta> herramientas = resultado.invocaciones().stream()
                    .map(i -> guardarEjecucion(conversacion, usuario, i)).toList();
            conversacion.setUltimaActividadEn(OffsetDateTime.now(ZoneOffset.UTC));
            conversaciones.save(conversacion);
            guardarAuditoria(conversacion, idUsuario, DecisionAuditoria.ALLOWED, "CHAT_COMPLETED", null,
                    Map.of("correlationId", correlacion.toString(), "tools", herramientas.size()));
            return new ChatResponse(conversacion.getId(), correlacion, mensajeMapper.aResponse(usuario),
                    mensajeMapper.aResponse(asistente), herramientas.stream().map(ejecucionMapper::aResponse).toList());
        } catch (RuntimeException ex) {
            guardarAuditoria(conversacion, idUsuario, DecisionAuditoria.ERROR, "CHAT_FAILED",
                    ex.getMessage(), Map.of("correlationId", correlacion.toString()));
            throw ex;
        }
    }

    private Conversacion obtenerOCrearConversacion(ChatRequest request, String idUsuario) {
        if (request.idConversacion() != null) {
            Conversacion existente = conversaciones.findById(request.idConversacion())
                    .orElseThrow(() -> new ConversacionNoEncontradaException(request.idConversacion()));
            if (!existente.getIdUsuarioObjeto().equals(idUsuario)) {
                guardarAuditoria(existente, idUsuario, DecisionAuditoria.DENIED, "CHAT_ACCESS",
                        "La conversación pertenece a otro usuario", Map.of());
                throw new SecurityException("No tiene acceso a esta conversación");
            }
            if (existente.getEstado() != EstadoConversacion.ACTIVE)
                throw new IllegalArgumentException("La conversación no está activa");
            return existente;
        }
        Conversacion nueva = new Conversacion();
        nueva.setIdUsuarioObjeto(idUsuario);
        nueva.setTitulo(request.mensaje().substring(0, Math.min(request.mensaje().length(), 100)));
        nueva.setEstado(EstadoConversacion.ACTIVE);
        nueva.setUltimaActividadEn(OffsetDateTime.now(ZoneOffset.UTC));
        return conversaciones.save(nueva);
    }

    private Mensaje guardarMensaje(Conversacion conversacion, RolMensaje rol, String contenido, String modelo,
                                    int entrada, int salida, UUID correlacion) {
        Mensaje mensaje = new Mensaje();
        mensaje.setConversacion(conversacion);
        mensaje.setRol(rol);
        mensaje.setContenido(contenido);
        mensaje.setNombreModelo(modelo);
        mensaje.setTokensEntrada(entrada);
        mensaje.setTokensSalida(salida);
        mensaje.setIdCorrelacion(correlacion);
        return mensajes.save(mensaje);
    }

    @SuppressWarnings("unchecked")
    private EjecucionHerramienta guardarEjecucion(Conversacion conversacion, Mensaje mensaje,
                                                    Map<String, Object> datos) {
        EjecucionHerramienta ejecucion = new EjecucionHerramienta();
        ejecucion.setConversacion(conversacion);
        ejecucion.setMensaje(mensaje);
        ejecucion.setNombreHerramienta(String.valueOf(datos.get("nombre")));
        ejecucion.setSolicitud(datos.get("argumentos") instanceof Map<?, ?> m ? (Map<String, Object>) m : Map.of());
        ejecucion.setRespuesta(datos.get("respuesta") instanceof Map<?, ?> m ? (Map<String, Object>) m : Map.of());
        ejecucion.setEstado(EstadoEjecucionHerramienta.valueOf(String.valueOf(datos.get("estado"))));
        ejecucion.setDuracionMs(datos.get("duracionMs") instanceof Number n ? n.intValue() : 0);
        ejecucion.setMensajeError(datos.get("error") == null ? null : String.valueOf(datos.get("error")));
        return ejecuciones.save(ejecucion);
    }

    private void guardarAuditoria(Conversacion conversacion, String usuario, DecisionAuditoria decision,
                                  String accion, String motivo, Map<String, Object> metadatos) {
        EventoAuditoria evento = new EventoAuditoria();
        evento.setConversacion(conversacion);
        evento.setIdUsuarioObjeto(usuario);
        evento.setAccion(accion);
        evento.setTipoRecurso("CONVERSATION");
        evento.setIdRecurso(conversacion.getId());
        evento.setDecision(decision);
        evento.setMotivo(motivo);
        evento.setMetadatos(metadatos);
        auditorias.save(evento);
    }
}
