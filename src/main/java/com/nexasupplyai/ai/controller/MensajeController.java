package com.nexasupplyai.ai.controller;

import com.nexasupplyai.ai.dto.response.MensajeResponse;
import com.nexasupplyai.ai.service.interfaces.MensajeServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mensajes")
@RequiredArgsConstructor
public class MensajeController {
    private final MensajeServiceInterface servicio;

    @GetMapping
    public List<MensajeResponse> listar(@RequestParam UUID idConversacion) {
        return servicio.listarPorConversacion(idConversacion);
    }

    @GetMapping("/{id}")
    public MensajeResponse obtener(@PathVariable UUID id) {
        return servicio.obtenerPorId(id);
    }
}
