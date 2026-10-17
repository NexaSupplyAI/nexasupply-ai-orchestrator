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
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/conversaciones")
@RequiredArgsConstructor
public class ConversacionController {
    private final ConversacionServiceInterface servicio;

    @GetMapping
    public List<ConversacionResponse> listar(Authentication authentication) {
        return servicio.listarPorUsuario(usuario(authentication));
    }

    @GetMapping("/{id}")
    public ConversacionResponse obtener(@PathVariable UUID id, Authentication authentication) {
        return servicio.obtenerPorId(id, usuario(authentication), esAdmin(authentication));
    }

    @PostMapping
    public ResponseEntity<ConversacionResponse> crear(@Valid @RequestBody ConversacionRequest r,
                                                       Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(r, usuario(authentication)));
    }

    private String usuario(Authentication authentication) {
        return authentication == null ? "local-demo-user" : authentication.getName();
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
