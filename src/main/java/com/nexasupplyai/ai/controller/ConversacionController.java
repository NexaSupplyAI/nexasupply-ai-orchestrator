package com.nexasupplyai.ai.controller;

import com.nexasupplyai.ai.dto.request.ConversacionRequest;
import com.nexasupplyai.ai.dto.response.ConversacionResponse;
import com.nexasupplyai.ai.service.interfaces.ConversacionServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conversaciones")
@RequiredArgsConstructor
public class ConversacionController {
    private final ConversacionServiceInterface servicio;

    @GetMapping
    public List<ConversacionResponse> listar(@RequestParam String idUsuario) {
        return servicio.listarPorUsuario(idUsuario);
    }

    @GetMapping("/{id}")
    public ConversacionResponse obtener(@PathVariable UUID id) {
        return servicio.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<ConversacionResponse> crear(@Valid @RequestBody ConversacionRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(r));
    }
}
