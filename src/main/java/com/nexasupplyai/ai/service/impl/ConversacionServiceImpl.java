package com.nexasupplyai.ai.service.impl;

import com.nexasupplyai.ai.dto.request.ConversacionRequest;
import com.nexasupplyai.ai.dto.response.ConversacionResponse;
import com.nexasupplyai.ai.entity.Conversacion;
import com.nexasupplyai.ai.exception.ConversacionNoEncontradaException;
import com.nexasupplyai.ai.mapper.ConversacionMapper;
import com.nexasupplyai.ai.repository.ConversacionRepository;
import com.nexasupplyai.ai.service.interfaces.ConversacionServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import com.nexasupplyai.ai.enums.EstadoConversacion;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConversacionServiceImpl implements ConversacionServiceInterface {

    private final ConversacionRepository repositorio;
    private final ConversacionMapper mapper;

    public List<ConversacionResponse> listarPorUsuario(String id) {
        return repositorio.findAllByIdUsuarioObjetoOrderByUltimaActividadEnDesc(id)
                .stream()
                .map(mapper::aResponse)
                .toList();
    }

    public ConversacionResponse obtenerPorId(UUID id, String idUsuario, boolean admin) {
        Conversacion conversacion = buscar(id);
        validarPropietario(conversacion, idUsuario, admin);
        return mapper.aResponse(conversacion);
    }

    @Transactional
    public ConversacionResponse crear(ConversacionRequest r, String idUsuario) {
        Conversacion conversacion = new Conversacion();
        conversacion.setIdUsuarioObjeto(idUsuario);
        conversacion.setTitulo(r.titulo());
        conversacion.setEstado(EstadoConversacion.ACTIVE);
        conversacion.setUltimaActividadEn(OffsetDateTime.now(ZoneOffset.UTC));
        return mapper.aResponse(repositorio.save(conversacion));
    }

    private Conversacion buscar(UUID id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ConversacionNoEncontradaException(id));
    }

    private void validarPropietario(Conversacion conversacion, String idUsuario, boolean admin) {
        if (!admin && !conversacion.getIdUsuarioObjeto().equals(idUsuario)) {
            throw new SecurityException("No tiene acceso a esta conversación");
        }
    }
}
