package com.nexasupplyai.ai.mapper;

import com.nexasupplyai.ai.dto.request.MensajeRequest;
import com.nexasupplyai.ai.dto.response.MensajeResponse;
import com.nexasupplyai.ai.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MensajeMapper {

    @Mapping(target = "idConversacion", source = "conversacion.id")
    MensajeResponse aResponse(Mensaje e);
}