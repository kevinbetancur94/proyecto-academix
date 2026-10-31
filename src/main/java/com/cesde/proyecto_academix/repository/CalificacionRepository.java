package com.cesde.proyecto_academix.repository;

import com.cesde.proyecto_academix.model.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    // Notas de un estudiante en todas sus materias
    List<Calificacion> findByEstudianteId(Long estudianteId);

    // Calificaciones de una materia (estudiantes inscritos en ella)
    List<Calificacion> findByMateriaId(Long materiaId);

    // La calificación de un estudiante en una materia (si ya existe se actualiza)
    Optional<Calificacion> findFirstByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
}
