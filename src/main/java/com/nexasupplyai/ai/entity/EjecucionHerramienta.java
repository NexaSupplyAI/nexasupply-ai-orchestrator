package com.nexasupplyai.ai.entity;

import com.nexasupplyai.ai.enums.EstadoEjecucionHerramienta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "tool_executions", schema = "ai")
@Getter
@Setter
@NoArgsConstructor
public class EjecucionHerramienta {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversacion conversacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Mensaje mensaje;

    @Column(name = "tool_name", nullable = false, length = 120)
    private String nombreHerramienta;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_payload", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> solicitud = Map.of();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_payload", columnDefinition = "jsonb")
    private Map<String, Object> respuesta;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EstadoEjecucionHerramienta estado;

    @Column(name = "duration_ms")
    private Integer duracionMs;

    @Column(name = "error_message", columnDefinition = "text")
    private String mensajeError;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;
}