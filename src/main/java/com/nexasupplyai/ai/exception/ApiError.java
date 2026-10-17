package com.nexasupplyai.ai.exception;import java.time.OffsetDateTime;import java.util.Map;
public record ApiError(int status,String error,String message,String path,String correlationId,OffsetDateTime timestamp,Map<String,String> fieldErrors){}
