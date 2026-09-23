package com.cesde.proyecto_academix.repository;

import com.cesde.proyecto_academix.model.entity.BoletinAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoletinAcademicoRepository extends JpaRepository<BoletinAcademico, Long> {

    // la necesitas para tu regla de "un boletín por estudiante"
    Optional<BoletinAcademico> findByEstudianteId(Long estudianteId);
}