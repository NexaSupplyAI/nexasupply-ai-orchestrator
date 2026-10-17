package com.nexasupplyai.ai.mapper;

import com.nexasupplyai.ai.dto.response.ConversacionResponse;
import com.nexasupplyai.ai.entity.Conversacion;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ConversacionMapper {

    ConversacionResponse aResponse(Conversacion entidad);
}
