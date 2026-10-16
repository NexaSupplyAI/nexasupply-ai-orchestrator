package com.nexasupplyai.ai.service.interfaces;

import com.nexasupplyai.ai.dto.response.EjecucionHerramientaResponse;

import java.util.List;
import java.util.UUID;

public interface EjecucionHerramientaServiceInterface {

    List<EjecucionHerramientaResponse> listarPorConversacion(UUID idConversacion);

    EjecucionHerramientaResponse obtenerPorId(UUID id);
}