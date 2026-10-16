package com.nexasupplyai.ai.entity;

import com.nexasupplyai.ai.enums.RolMensaje;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages", schema = "ai")
@Getter
@Setter
@NoArgsConstructor
public class Mensaje {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversacion conversacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private RolMensaje rol;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String contenido;

    @Column(name = "model_name", length = 120)
    private String nombreModelo;

    @Column(name = "input_tokens", nullable = false)
    private Integer tokensEntrada = 0;

    @Column(name = "output_tokens", nullable = false)
    private Integer tokensSalida = 0;

    @Column(name = "correlation_id", nullable = false)
    private UUID idCorrelacion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;
}