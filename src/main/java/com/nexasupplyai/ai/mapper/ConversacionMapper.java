package com.nexasupplyai.ai.mapper;

import com.nexasupplyai.ai.dto.request.ConversacionRequest;
import com.nexasupplyai.ai.dto.response.ConversacionResponse;
import com.nexasupplyai.ai.entity.Conversacion;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ConversacionMapper {

    @Mapping(target = "estado", source = "estado", defaultValue = "ACTIVE")
    @Mapping(target = "ultimaActividadEn", expression = "java(java.time.OffsetDateTime.now(java.time.ZoneOffset.UTC))")
    Conversacion aEntidad(ConversacionRequest request);

    void actualizarEntidad(
            ConversacionRequest request,
            @MappingTarget Conversacion entidad
    );

    ConversacionResponse aResponse(Conversacion entidad);
}
