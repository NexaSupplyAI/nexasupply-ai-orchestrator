package com.nexasupplyai.ai.controller;

import com.nexasupplyai.ai.dto.request.ChatRequest;
import com.nexasupplyai.ai.dto.response.ChatResponse;
import com.nexasupplyai.ai.service.impl.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/v1/ai/chat", "/api/v1/chat"})
@RequiredArgsConstructor
public class ChatController {
    private final ChatService servicio;

    @PostMapping
    public ChatResponse conversar(@Valid @RequestBody ChatRequest request, Authentication authentication) {
        return servicio.conversar(request, authentication == null ? "local-demo-user" : authentication.getName());
    }
}
