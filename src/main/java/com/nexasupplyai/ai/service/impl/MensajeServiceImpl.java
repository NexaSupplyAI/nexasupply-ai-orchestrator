package com.nexasupplyai.ai.service.impl;

import com.nexasupplyai.ai.dto.response.MensajeResponse;
import com.nexasupplyai.ai.entity.Mensaje;
import com.nexasupplyai.ai.exception.MensajeNoEncontradoException;
import com.nexasupplyai.ai.mapper.MensajeMapper;
import com.nexasupplyai.ai.repository.MensajeRepository;
import com.nexasupplyai.ai.service.interfaces.MensajeServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MensajeServiceImpl implements MensajeServiceInterface {

    private final MensajeRepository repositorio;
    private final MensajeMapper mapper;

    public List<MensajeResponse> listarPorConversacion(UUID id) {
        return repositorio.findAllByConversacionIdOrderByCreadoEnAsc(id)
                .stream()
                .map(mapper::aResponse)
                .toList();
    }

    public MensajeResponse obtenerPorId(UUID id) {
        Mensaje e = repositorio.findById(id)
                .orElseThrow(() -> new MensajeNoEncontradoException(id));

        return mapper.aResponse(e);
    }
}
