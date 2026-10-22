package com.cesde.proyecto_academix.repository;

import com.cesde.proyecto_academix.model.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByMateriaId(Long materiaId);
}
