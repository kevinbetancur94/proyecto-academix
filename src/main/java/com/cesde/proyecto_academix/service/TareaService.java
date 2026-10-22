package com.cesde.proyecto_academix.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.proyecto_academix.dto.TareaRequestDTO;
import com.cesde.proyecto_academix.dto.TareaResponseDTO;
import com.cesde.proyecto_academix.exception.RecursoNoEncontradoException;
import com.cesde.proyecto_academix.exception.ReglaDeNegocioException;
import com.cesde.proyecto_academix.model.entity.Materia;
import com.cesde.proyecto_academix.model.entity.Tarea;
import com.cesde.proyecto_academix.repository.TareaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final MateriaService materiaService;

    public List<TareaResponseDTO> listarTodas() {
        return tareaRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public TareaResponseDTO obtenerPorId(Long id) {
        return convertirADTO(obtenerEntidadPorId(id));
    }

    private Tarea obtenerEntidadPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una tarea con el id " + id));
    }

    public TareaResponseDTO crear(TareaRequestDTO dto) {
        Materia materia = validarMateriaActiva(dto);
        validarFecha(dto.fecha());

        Tarea tarea = new Tarea();
        copiarDatos(dto, tarea, materia);

        return convertirADTO(tareaRepository.save(tarea));
    }

    public TareaResponseDTO actualizar(Long id, TareaRequestDTO dto) {
        Tarea tarea = obtenerEntidadPorId(id);

        Materia materia = validarMateriaActiva(dto);
        validarFecha(dto.fecha());
        copiarDatos(dto, tarea, materia);

        return convertirADTO(tareaRepository.save(tarea));
    }

    public void eliminar(Long id) {
        tareaRepository.delete(obtenerEntidadPorId(id));
    }

    // Regla de negocio 1: la fecha de la tarea no puede ser anterior a hoy.
    private void validarFecha(LocalDate fecha) {
        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            throw new ReglaDeNegocioException("La fecha de la tarea no puede ser anterior al día de hoy");
        }
    }

    // Regla de negocio 2: la materia asociada debe existir y estar activa.
    private Materia validarMateriaActiva(TareaRequestDTO dto) {
        if (dto.materiaId() == null) {
            throw new ReglaDeNegocioException("La tarea debe estar asociada a una materia");
        }

        Materia materia = materiaService.obtenerEntidadPorId(dto.materiaId());

        if (!Boolean.TRUE.equals(materia.getEstadoActivo())) {
            throw new ReglaDeNegocioException("No se pueden asignar tareas a una materia inactiva");
        }
        return materia;
    }

    private void copiarDatos(TareaRequestDTO dto, Tarea tarea, Materia materia) {
        tarea.setTitulo(dto.titulo());
        tarea.setDescripcion(dto.descripcion());
        tarea.setMateria(materia);
        tarea.setTipo(dto.tipo());
        tarea.setGrado(dto.grado());
        tarea.setFecha(dto.fecha());
        tarea.setObservaciones(dto.observaciones());
    }

    private TareaResponseDTO convertirADTO(Tarea t) {
        return new TareaResponseDTO(t.getId(), t.getTitulo(), t.getDescripcion(),
                t.getMateria().getId(), t.getMateria().getNombre(),
                t.getTipo(), t.getGrado(), t.getFecha(), t.getObservaciones(), t.getEstadoActivo());
    }
}
