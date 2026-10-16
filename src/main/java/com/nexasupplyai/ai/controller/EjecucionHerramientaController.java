package com.nexasupplyai.ai.controller;

import com.nexasupplyai.ai.dto.response.EjecucionHerramientaResponse;
import com.nexasupplyai.ai.service.interfaces.EjecucionHerramientaServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ejecuciones-herramienta")
@RequiredArgsConstructor
public class EjecucionHerramientaController {
    private final EjecucionHerramientaServiceInterface servicio;

    @GetMapping
    public List<EjecucionHerramientaResponse> listar(@RequestParam UUID idConversacion) {
        return servicio.listarPorConversacion(idConversacion);
    }

    @GetMapping("/{id}")
    public EjecucionHerramientaResponse obtener(@PathVariable UUID id) {
        return servicio.obtenerPorId(id);
    }
}
