package com.nexasupplyai.ai.repository;

import com.nexasupplyai.ai.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, UUID> {

    List<Mensaje> findAllByConversacionIdOrderByCreadoEnAsc(UUID idConversacion);

    List<Mensaje> findAllByIdCorrelacion(UUID idCorrelacion);
}