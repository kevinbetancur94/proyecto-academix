package com.cesde.proyecto_academix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.proyecto_academix.dto.CalificacionRequestDTO;
import com.cesde.proyecto_academix.dto.CalificacionResponseDTO;
import com.cesde.proyecto_academix.exception.RecursoNoEncontradoException;
import com.cesde.proyecto_academix.exception.ReglaDeNegocioException;
import com.cesde.proyecto_academix.model.entity.Calificacion;
import com.cesde.proyecto_academix.model.entity.Materia;
import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.repository.CalificacionRepository;
import lombok.RequiredArgsConstructor;

// Calificacion depende de Usuario y Materia, por eso este Service inyecta
// UsuarioService y MateriaService (NO sus repositories directamente).
@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final UsuarioService usuarioService;
    private final MateriaService materiaService;

    public List<CalificacionResponseDTO> listarTodos() {
        return calificacionRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public CalificacionResponseDTO obtenerPorId(Long id) {
        return convertirADTO(obtenerEntidadPorId(id));
    }

    private Calificacion obtenerEntidadPorId(Long id) {
        return calificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una calificación con id " + id));
    }

    public CalificacionResponseDTO crear(CalificacionRequestDTO dto) {
        if (dto.estudianteId() == null) {
            throw new ReglaDeNegocioException("La calificación debe estar asociada a un estudiante");
        }
        if (dto.materiaId() == null) {
            throw new ReglaDeNegocioException("La calificación debe estar asociada a una materia");
        }

        Usuario estudiante = usuarioService.obtenerEntidadPorId(dto.estudianteId());
        Materia materia = materiaService.obtenerEntidadPorId(dto.materiaId());

        validarRolEstudiante(estudiante);
        validarNotaEnRango(dto.act1(), "actividad 1");
        validarNotaEnRango(dto.act2(), "actividad 2");
        validarNotaEnRango(dto.act3(), "actividad 3");

        Calificacion calificacion = new Calificacion();
        calificacion.setEstudiante(estudiante);
        calificacion.setMateria(materia);
        calificacion.setAct1(dto.act1());
        calificacion.setAct2(dto.act2());
        calificacion.setAct3(dto.act3());
        calificacion.setObservacion(dto.observacion());

        return convertirADTO(calificacionRepository.save(calificacion));
    }

    // Actualización parcial: solo se modifican los campos que vienen en el DTO (no nulos)
    public CalificacionResponseDTO actualizar(Long id, CalificacionRequestDTO dto) {
        Calificacion existente = obtenerEntidadPorId(id);

        if (dto.estudianteId() != null) {
            Usuario estudiante = usuarioService.obtenerEntidadPorId(dto.estudianteId());
            validarRolEstudiante(estudiante);
            existente.setEstudiante(estudiante);
        }
        if (dto.materiaId() != null) {
            existente.setMateria(materiaService.obtenerEntidadPorId(dto.materiaId()));
        }
        if (dto.act1() != null) {
            existente.setAct1(dto.act1());
        }
        if (dto.act2() != null) {
            existente.setAct2(dto.act2());
        }
        if (dto.act3() != null) {
            existente.setAct3(dto.act3());
        }
        if (dto.observacion() != null) {
            existente.setObservacion(dto.observacion());
        }

        validarNotaEnRango(existente.getAct1(), "actividad 1");
        validarNotaEnRango(existente.getAct2(), "actividad 2");
        validarNotaEnRango(existente.getAct3(), "actividad 3");

        return convertirADTO(calificacionRepository.save(existente));
    }

    public void eliminar(Long id) {
        calificacionRepository.delete(obtenerEntidadPorId(id));
    }

    // Regla de negocio 1: el Usuario asignado como estudiante debe tener rol ESTUDIANTE.
    private void validarRolEstudiante(Usuario estudiante) {
        if (estudiante.getRol() != RolUsuario.ESTUDIANTE) {
            throw new ReglaDeNegocioException("El usuario asignado como estudiante no tiene rol ESTUDIANTE");
        }
    }

    // Regla de negocio 2: las notas de las 3 actividades deben estar entre 0.0 y 5.0.
    private void validarNotaEnRango(Double nota, String nombreCampo) {
        if (nota == null || nota < 0.0 || nota > 5.0) {
            throw new ReglaDeNegocioException("La nota de " + nombreCampo + " debe estar entre 0.0 y 5.0");
        }
    }

    private CalificacionResponseDTO convertirADTO(Calificacion c) {
        double promedio = (c.getAct1() + c.getAct2() + c.getAct3()) / 3.0;
        return new CalificacionResponseDTO(c.getId(),
                c.getEstudiante().getId(), c.getEstudiante().getNombre(),
                c.getMateria().getId(), c.getMateria().getNombre(),
                c.getAct1(), c.getAct2(), c.getAct3(), promedio, c.getObservacion());
    }
}
