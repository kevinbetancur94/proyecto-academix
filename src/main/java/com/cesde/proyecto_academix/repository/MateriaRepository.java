package com.cesde.proyecto_academix.repository;

import com.cesde.proyecto_academix.model.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    Optional<Materia> findByCodigo(String codigo);

    // Calendario: el frontend envía la materia por su nombre ("Matemáticas")
    Optional<Materia> findFirstByNombreIgnoreCase(String nombre);

    // Panel docente: materias que dicta un docente
    List<Materia> findByDocenteIdOrderByNombreAsc(Long docenteId);
}
