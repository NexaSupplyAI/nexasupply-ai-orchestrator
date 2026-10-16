package com.nexasupplyai.ai.service.interfaces;

import com.nexasupplyai.ai.dto.response.EventoAuditoriaResponse;

import java.util.List;
import java.util.UUID;

public interface EventoAuditoriaServiceInterface {

    List<EventoAuditoriaResponse> listarPorUsuario(String idUsuario);

    EventoAuditoriaResponse obtenerPorId(UUID id);
}