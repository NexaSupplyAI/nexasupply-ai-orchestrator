package com.nexasupplyai.ai.controller;

import com.nexasupplyai.ai.dto.response.MensajeResponse;
import com.nexasupplyai.ai.service.interfaces.MensajeServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/mensajes")
@RequiredArgsConstructor
public class MensajeController {
    private final MensajeServiceInterface servicio;

    @GetMapping
    public List<MensajeResponse> listar(@RequestParam UUID idConversacion, Authentication authentication) {
        return servicio.listarPorConversacion(idConversacion, usuario(authentication), esAdmin(authentication));
    }

    @GetMapping("/{id}")
    public MensajeResponse obtener(@PathVariable UUID id, Authentication authentication) {
        return servicio.obtenerPorId(id, usuario(authentication), esAdmin(authentication));
    }
    private String usuario(Authentication a) { return a == null ? "local-demo-user" : a.getName(); }
    private boolean esAdmin(Authentication a) { return a != null && a.getAuthorities().stream()
            .anyMatch(x -> "ROLE_ADMIN".equals(x.getAuthority())); }
}
