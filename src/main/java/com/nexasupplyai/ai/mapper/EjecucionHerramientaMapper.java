package com.nexasupplyai.ai.mapper;

import com.nexasupplyai.ai.dto.response.EjecucionHerramientaResponse;
import com.nexasupplyai.ai.entity.EjecucionHerramienta;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EjecucionHerramientaMapper {

    @Mapping(target = "idConversacion", source = "conversacion.id")
    @Mapping(target = "idMensaje", source = "mensaje.id")
    EjecucionHerramientaResponse aResponse(EjecucionHerramienta e);
}