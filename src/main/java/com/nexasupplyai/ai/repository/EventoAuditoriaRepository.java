package com.nexasupplyai.ai.repository;

import com.nexasupplyai.ai.entity.EventoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventoAuditoriaRepository extends JpaRepository<EventoAuditoria, UUID> {

    List<EventoAuditoria> findAllByIdUsuarioObjetoOrderByCreadoEnDesc(String idUsuarioObjeto);
}
