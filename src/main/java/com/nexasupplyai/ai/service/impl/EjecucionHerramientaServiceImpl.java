package com.nexasupplyai.ai.service.impl;

import com.nexasupplyai.ai.dto.response.EjecucionHerramientaResponse;
import com.nexasupplyai.ai.entity.EjecucionHerramienta;
import com.nexasupplyai.ai.mapper.EjecucionHerramientaMapper;
import com.nexasupplyai.ai.repository.EjecucionHerramientaRepository;
import com.nexasupplyai.ai.service.interfaces.EjecucionHerramientaServiceInterface;
import com.nexasupplyai.ai.exception.EjecucionHerramientaNoEncontradaException;
import com.nexasupplyai.ai.repository.ConversacionRepository;
import com.nexasupplyai.ai.exception.ConversacionNoEncontradaException;
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
    private final ConversacionRepository conversaciones;

    public List<EjecucionHerramientaResponse> listarPorConversacion(UUID id, String idUsuario, boolean admin) {
        var conversacion = conversaciones.findById(id).orElseThrow(() -> new ConversacionNoEncontradaException(id));
        validar(conversacion.getIdUsuarioObjeto(), idUsuario, admin);
        return repositorio.findAllByConversacionIdOrderByCreadoEnDesc(id)
                .stream()
                .map(mapper::aResponse)
                .toList();
    }

    public EjecucionHerramientaResponse obtenerPorId(UUID id, String idUsuario, boolean admin) {
        EjecucionHerramienta e = repositorio.findById(id)
                .orElseThrow(() -> new EjecucionHerramientaNoEncontradaException(id));
        validar(e.getConversacion().getIdUsuarioObjeto(), idUsuario, admin);
        return mapper.aResponse(e);
    }
    private void validar(String propietario, String idUsuario, boolean admin) {
        if (!admin && !propietario.equals(idUsuario)) throw new SecurityException("No tiene acceso a esta ejecución");
    }
}
