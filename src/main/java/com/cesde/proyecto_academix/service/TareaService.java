package com.cesde.proyecto_academix.service;

import com.cesde.proyecto_academix.model.entity.Materia;
import com.cesde.proyecto_academix.model.entity.Tarea;
import com.cesde.proyecto_academix.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final MateriaService materiaService;

    public List<Tarea> listarTodas() {
        return tareaRepository.findAll();
    }

    public Tarea obtenerPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una tarea con el id " + id));
    }

    public Tarea crear(Tarea tarea) {
        Materia materia = validarMateriaActiva(tarea);
        validarFecha(tarea.getFecha());
        tarea.setMateria(materia);
        return tareaRepository.save(tarea);
    }

    public Tarea actualizar(Long id, Tarea datos) {
        Tarea tarea = obtenerPorId(id);

        Materia materia = validarMateriaActiva(datos);
        validarFecha(datos.getFecha());

        tarea.setTitulo(datos.getTitulo());
        tarea.setDescripcion(datos.getDescripcion());
        tarea.setMateria(materia);
        tarea.setTipo(datos.getTipo());
        tarea.setGrado(datos.getGrado());
        tarea.setFecha(datos.getFecha());
        tarea.setObservaciones(datos.getObservaciones());

        return tareaRepository.save(tarea);
    }

    public void eliminar(Long id) {
        Tarea tarea = obtenerPorId(id);
        tareaRepository.delete(tarea);
    }

    // Regla de negocio 1: la fecha de la tarea no puede ser anterior a hoy.
    private void validarFecha(LocalDate fecha) {
        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la tarea no puede ser anterior al día de hoy");
        }
    }

    // Regla de negocio 2: la materia asociada debe existir y estar activa.
    private Materia validarMateriaActiva(Tarea tarea) {
        if (tarea.getMateria() == null || tarea.getMateria().getId() == null) {
            throw new IllegalArgumentException("La tarea debe estar asociada a una materia");
        }

        Materia materia = materiaService.obtenerPorId(tarea.getMateria().getId());

        if (!Boolean.TRUE.equals(materia.getEstadoActivo())) {
            throw new IllegalArgumentException("No se pueden asignar tareas a una materia inactiva");
        }

        return materia;
    }
}
