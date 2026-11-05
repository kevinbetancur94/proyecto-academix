package com.cesde.proyecto_academix.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cesde.proyecto_academix.model.entity.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByMateriaId(Long materiaId);

    // Tareas de un grado ordenadas por fecha
    List<Tarea> findByGradoOrderByFechaAsc(String grado);

    // Tareas pendientes: desde una fecha en adelante
    @Query("SELECT t FROM Tarea t WHERE t.fecha >= :desde ORDER BY t.fecha ASC")
    List<Tarea> buscarProximas(@Param("desde") LocalDate desde);
}
