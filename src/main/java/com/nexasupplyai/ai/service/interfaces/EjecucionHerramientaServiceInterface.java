package com.nexasupplyai.ai.service.interfaces;

import com.nexasupplyai.ai.dto.response.EjecucionHerramientaResponse;

import java.util.List;
import java.util.UUID;

public interface EjecucionHerramientaServiceInterface {

    List<EjecucionHerramientaResponse> listarPorConversacion(UUID idConversacion, String idUsuario, boolean admin);

    EjecucionHerramientaResponse obtenerPorId(UUID id, String idUsuario, boolean admin);
}
