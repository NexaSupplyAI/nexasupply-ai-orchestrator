package com.nexasupplyai.ai.service.impl;

import com.nexasupplyai.ai.dto.response.EjecucionHerramientaResponse;
import com.nexasupplyai.ai.entity.EjecucionHerramienta;
import com.nexasupplyai.ai.mapper.EjecucionHerramientaMapper;
import com.nexasupplyai.ai.repository.EjecucionHerramientaRepository;
import com.nexasupplyai.ai.service.interfaces.EjecucionHerramientaServiceInterface;
import com.nexasupplyai.ai.exception.EjecucionHerramientaNoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EjecucionHerramientaServiceImpl implements EjecucionHerramientaServiceInterface {

    private final EjecucionHerramientaRepository repositorio;
    private final EjecucionHerramientaMapper mapper;

    public List<EjecucionHerramientaResponse> listarPorConversacion(UUID id) {
        return repositorio.findAllByConversacionIdOrderByCreadoEnDesc(id)
                .stream()
                .map(mapper::aResponse)
                .toList();
    }

    public EjecucionHerramientaResponse obtenerPorId(UUID id) {
        EjecucionHerramienta e = repositorio.findById(id)
                .orElseThrow(() -> new EjecucionHerramientaNoEncontradaException(id));

        return mapper.aResponse(e);
    }
}
