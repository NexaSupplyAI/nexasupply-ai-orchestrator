package com.nexasupplyai.ai.service.impl;

import com.nexasupplyai.ai.dto.response.EventoAuditoriaResponse;
import com.nexasupplyai.ai.entity.EventoAuditoria;
import com.nexasupplyai.ai.mapper.EventoAuditoriaMapper;
import com.nexasupplyai.ai.repository.EventoAuditoriaRepository;
import com.nexasupplyai.ai.service.interfaces.EventoAuditoriaServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoAuditoriaServiceImpl implements EventoAuditoriaServiceInterface {

    private final EventoAuditoriaRepository repositorio;
    private final EventoAuditoriaMapper mapper;

    public List<EventoAuditoriaResponse> listarPorUsuario(String id) {
        return repositorio.findAllByIdUsuarioObjetoOrderByCreadoEnDesc(id)
                .stream()
                .map(mapper::aResponse)
                .toList();
    }

    public EventoAuditoriaResponse obtenerPorId(UUID id) {
        EventoAuditoria e = repositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Evento no encontrado: " + id
                ));

        return mapper.aResponse(e);
    }
}