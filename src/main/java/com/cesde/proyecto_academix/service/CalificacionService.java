package com.cesde.proyecto_academix.service;

import com.cesde.proyecto_academix.model.entity.Calificacion;
import com.cesde.proyecto_academix.model.entity.Materia;
import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.repository.CalificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Va en: src/main/java/com/cesde/proyecto_academix/service/CalificacionService.java
//
// Calificacion depende de Usuario y Materia, por eso este Service inyecta
// UsuarioService y MateriaService (NO sus repository directamente) — así respeta
// la capa de negocio de esas entidades, tal como explica la guía de estudio (secc. 1).
//
// IMPORTANTE: para que esto compile, UsuarioService y MateriaService ya deben tener
// un método obtenerPorId(Long id). Si Kev o quien haga Materia todavía no lo ha
// subido, puedes escribir este archivo igual y ajustarlo cuando su rama se actualice
// (así lo dice la guía de reparto, sección "Orden de trabajo").
@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final UsuarioService usuarioService;
    private final MateriaService materiaService;

    public List<Calificacion> listarTodos() {
        return calificacionRepository.findAll();
    }

    public Calificacion obtenerPorId(Long id) {
        return calificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una calificación con id " + id));
    }

    public Calificacion crear(Calificacion calificacion) {
        Usuario estudiante = usuarioService.obtenerPorId(calificacion.getEstudiante().getId());
        Materia materia = materiaService.obtenerPorId(calificacion.getMateria().getId());

        validarRolEstudiante(estudiante);
        validarNotasEnRango(calificacion);

        calificacion.setEstudiante(estudiante);
        calificacion.setMateria(materia);
        return calificacionRepository.save(calificacion);
    }

    public Calificacion actualizar(Long id, Calificacion datos) {
        Calificacion existente = obtenerPorId(id);

        if (datos.getEstudiante() != null) {
            Usuario estudiante = usuarioService.obtenerPorId(datos.getEstudiante().getId());
            validarRolEstudiante(estudiante);
            existente.setEstudiante(estudiante);
        }
        if (datos.getMateria() != null) {
            Materia materia = materiaService.obtenerPorId(datos.getMateria().getId());
            existente.setMateria(materia);
        }
        if (datos.getAct1() != null) {
            existente.setAct1(datos.getAct1());
        }
        if (datos.getAct2() != null) {
            existente.setAct2(datos.getAct2());
        }
        if (datos.getAct3() != null) {
            existente.setAct3(datos.getAct3());
        }
        if (datos.getObservacion() != null) {
            existente.setObservacion(datos.getObservacion());
        }

        validarNotasEnRango(existente);

        return calificacionRepository.save(existente);
    }

    public void eliminar(Long id) {
        Calificacion existente = obtenerPorId(id);
        calificacionRepository.delete(existente);
    }

    // Regla de negocio 1: el Usuario asignado como estudiante debe tener rol ESTUDIANTE
    // (evita que por error se le asigne una calificación a un docente o acudiente).
    private void validarRolEstudiante(Usuario estudiante) {
        if (estudiante.getRol() != RolUsuario.ESTUDIANTE) {
            throw new IllegalArgumentException("El usuario asignado como estudiante no tiene rol ESTUDIANTE");
        }
    }

    // Regla de negocio 2: las notas de las 3 actividades deben estar entre 0.0 y 5.0.
    private void validarNotasEnRango(Calificacion calificacion) {
        validarNotaEnRango(calificacion.getAct1(), "actividad 1");
        validarNotaEnRango(calificacion.getAct2(), "actividad 2");
        validarNotaEnRango(calificacion.getAct3(), "actividad 3");
    }

    private void validarNotaEnRango(Double nota, String nombreCampo) {
        if (nota == null || nota < 0.0 || nota > 5.0) {
            throw new IllegalArgumentException("La nota de " + nombreCampo + " debe estar entre 0.0 y 5.0");
        }
    }
}
