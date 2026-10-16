package com.nexasupplyai.ai.mapper;

import com.nexasupplyai.ai.dto.request.EventoAuditoriaRequest;
import com.nexasupplyai.ai.dto.response.EventoAuditoriaResponse;
import com.nexasupplyai.ai.entity.EventoAuditoria;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EventoAuditoriaMapper {

    @Mapping(target = "idConversacion", source = "conversacion.id")
    EventoAuditoriaResponse aResponse(EventoAuditoria e);
}