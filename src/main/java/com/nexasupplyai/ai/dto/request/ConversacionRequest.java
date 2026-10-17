package com.nexasupplyai.ai.dto.request;
import jakarta.validation.constraints.Size;
public record ConversacionRequest(@Size(max = 220) String titulo) { }
