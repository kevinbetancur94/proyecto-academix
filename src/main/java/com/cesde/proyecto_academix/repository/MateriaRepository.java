package com.cesde.proyecto_academix.repository;

import com.cesde.proyecto_academix.model.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    Optional<Materia> findByCodigo(String codigo);
}
