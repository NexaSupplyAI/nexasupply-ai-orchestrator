package com.nexasupplyai.ai.repository;

import com.nexasupplyai.ai.entity.EjecucionHerramienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EjecucionHerramientaRepository extends JpaRepository<EjecucionHerramienta, UUID> {

    List<EjecucionHerramienta> findAllByConversacionIdOrderByCreadoEnDesc(UUID idConversacion);

}
