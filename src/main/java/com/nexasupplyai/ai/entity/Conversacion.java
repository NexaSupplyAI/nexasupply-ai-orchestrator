package com.nexasupplyai.ai.entity;

import com.nexasupplyai.ai.enums.EstadoConversacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations", schema = "ai")
@Getter
@Setter
@NoArgsConstructor
public class Conversacion {

 @Id
 @UuidGenerator
 @Column(name = "id", updatable = false)
 private UUID id;

 @Column(name = "user_object_id", nullable = false, length = 120)
 private String idUsuarioObjeto;

 @Column(name = "title", length = 220)
 private String titulo;

 @Enumerated(EnumType.STRING)
 @Column(name = "status", nullable = false, length = 20)
 private EstadoConversacion estado = EstadoConversacion.ACTIVA;

 @CreationTimestamp
 @Column(name = "started_at", nullable = false, updatable = false)
 private OffsetDateTime iniciadaEn;

 @Column(name = "last_activity_at", nullable = false)
 private OffsetDateTime ultimaActividadEn;

 @Column(name = "closed_at")
 private OffsetDateTime cerradaEn;
}