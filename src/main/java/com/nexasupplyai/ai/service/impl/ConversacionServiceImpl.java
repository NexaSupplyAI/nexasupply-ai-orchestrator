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

    public ConversacionResponse obtenerPorId(UUID id) {
        return mapper.aResponse(buscar(id));
    }

    @Transactional
    public ConversacionResponse crear(ConversacionRequest r) {
        return mapper.aResponse(
                repositorio.save(mapper.aEntidad(r))
        );
    }

    private Conversacion buscar(UUID id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ConversacionNoEncontradaException(id));
    }
}
