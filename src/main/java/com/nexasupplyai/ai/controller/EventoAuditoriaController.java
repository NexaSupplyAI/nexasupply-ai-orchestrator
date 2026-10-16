package com.nexasupplyai.ai.controller;

import com.nexasupplyai.ai.dto.response.EventoAuditoriaResponse;
import com.nexasupplyai.ai.service.interfaces.EventoAuditoriaServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/eventos-auditoria")
@RequiredArgsConstructor
public class EventoAuditoriaController {
    private final EventoAuditoriaServiceInterface servicio;

    @GetMapping
    public List<EventoAuditoriaResponse> listar(@RequestParam String idUsuario) {
        return servicio.listarPorUsuario(idUsuario);
    }

    @GetMapping("/{id}")
    public EventoAuditoriaResponse obtener(@PathVariable UUID id) {
        return servicio.obtenerPorId(id);
    }
}
