package com.nexasupplyai.ai.service.impl;

import com.nexasupplyai.ai.dto.response.EventoAuditoriaResponse;
import com.nexasupplyai.ai.entity.EventoAuditoria;
import com.nexasupplyai.ai.mapper.EventoAuditoriaMapper;
import com.nexasupplyai.ai.repository.EventoAuditoriaRepository;
import com.nexasupplyai.ai.service.interfaces.EventoAuditoriaServiceInterface;
import com.nexasupplyai.ai.exception.EventoAuditoriaNoEncontradoException;
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

    public EventoAuditoriaResponse obtenerPorId(UUID id, String idUsuario, boolean admin) {
        EventoAuditoria e = repositorio.findById(id)
                .orElseThrow(() -> new EventoAuditoriaNoEncontradoException(id));
        if (!admin && !e.getIdUsuarioObjeto().equals(idUsuario)) {
            throw new SecurityException("No tiene acceso a este evento de auditoría");
        }
        return mapper.aResponse(e);
    }
}
