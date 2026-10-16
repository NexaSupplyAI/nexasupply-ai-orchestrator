package com.nexasupplyai.ai.entity;

import com.nexasupplyai.ai.enums.DecisionAuditoria;
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
@Table(name = "audit_events", schema = "ai")
@Getter
@Setter
@NoArgsConstructor
public class EventoAuditoria {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversacion conversacion;

    @Column(name = "user_object_id", nullable = false, length = 120)
    private String idUsuarioObjeto;

    @Column(name = "action", nullable = false, length = 120)
    private String accion;

    @Column(name = "resource_type", length = 80)
    private String tipoRecurso;

    @Column(name = "resource_id")
    private UUID idRecurso;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 20)
    private DecisionAuditoria decision;

    @Column(name = "reason", columnDefinition = "text")
    private String motivo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> metadatos = Map.of();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;
}