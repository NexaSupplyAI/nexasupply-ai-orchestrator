package com.nexasupplyai.ai.service.interfaces;

import com.nexasupplyai.ai.dto.request.ConversacionRequest;
import com.nexasupplyai.ai.dto.response.ConversacionResponse;

import java.util.List;
import java.util.UUID;

public interface ConversacionServiceInterface {

    List<ConversacionResponse> listarPorUsuario(String idUsuario);

    ConversacionResponse obtenerPorId(UUID id, String idUsuario, boolean admin);

    ConversacionResponse crear(ConversacionRequest request, String idUsuario);
}
