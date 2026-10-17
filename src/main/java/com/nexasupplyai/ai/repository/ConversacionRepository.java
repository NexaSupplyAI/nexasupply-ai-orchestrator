package com.nexasupplyai.ai.repository;

import com.nexasupplyai.ai.entity.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, UUID> {

    List<Conversacion> findAllByIdUsuarioObjetoOrderByUltimaActividadEnDesc(String idUsuarioObjeto);

}
