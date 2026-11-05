package com.cesde.proyecto_academix.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cesde.proyecto_academix.model.entity.BoletinAcademico;

public interface BoletinAcademicoRepository extends JpaRepository<BoletinAcademico, Long> {

    // Regla de "un boletín por estudiante"
    Optional<BoletinAcademico> findByEstudianteId(Long estudianteId);

    // Boletines de estudiantes con materias en riesgo, de mayor a menor riesgo
    List<BoletinAcademico> findByMateriasEnRiesgoGreaterThanOrderByMateriasEnRiesgoDesc(Integer minimo);

    // Boletines con promedio general mínimo, del mejor al peor
    @Query("SELECT b FROM BoletinAcademico b WHERE b.promedioGeneral >= :minimo ORDER BY b.promedioGeneral DESC")
    List<BoletinAcademico> buscarConPromedioMinimo(@Param("minimo") Double minimo);
}
