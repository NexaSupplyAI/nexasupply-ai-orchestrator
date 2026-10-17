package com.nexasupplyai.ai.service.interfaces;

import com.nexasupplyai.ai.dto.response.MensajeResponse;

import java.util.List;
import java.util.UUID;

public interface MensajeServiceInterface {

    List<MensajeResponse> listarPorConversacion(UUID idConversacion, String idUsuario, boolean admin);

    MensajeResponse obtenerPorId(UUID id, String idUsuario, boolean admin);
}
