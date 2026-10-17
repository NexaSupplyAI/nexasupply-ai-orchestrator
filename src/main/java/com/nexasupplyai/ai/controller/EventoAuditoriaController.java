package com.nexasupplyai.ai.controller;

import com.nexasupplyai.ai.dto.response.EventoAuditoriaResponse;
import com.nexasupplyai.ai.service.interfaces.EventoAuditoriaServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/eventos-auditoria")
@RequiredArgsConstructor
public class EventoAuditoriaController {
    private final EventoAuditoriaServiceInterface servicio;

    @GetMapping
    public List<EventoAuditoriaResponse> listar(Authentication authentication) {
        return servicio.listarPorUsuario(usuario(authentication));
    }

    @GetMapping("/{id}")
    public EventoAuditoriaResponse obtener(@PathVariable UUID id, Authentication authentication) {
        return servicio.obtenerPorId(id, usuario(authentication), esAdmin(authentication));
    }
    private String usuario(Authentication a) { return a == null ? "local-demo-user" : a.getName(); }
    private boolean esAdmin(Authentication a) { return a != null && a.getAuthorities().stream()
            .anyMatch(x -> "ROLE_ADMIN".equals(x.getAuthority())); }
}
