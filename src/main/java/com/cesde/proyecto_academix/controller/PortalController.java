package com.cesde.proyecto_academix.controller;

import com.cesde.proyecto_academix.dto.portal.EstudianteNotasDTO;
import com.cesde.proyecto_academix.dto.portal.FestivoDTO;
import com.cesde.proyecto_academix.dto.portal.GuardarCalificacionesDTO;
import com.cesde.proyecto_academix.dto.portal.HijoDTO;
import com.cesde.proyecto_academix.dto.portal.MateriaDocenteDTO;
import com.cesde.proyecto_academix.dto.portal.ResultadoGuardadoDTO;
import com.cesde.proyecto_academix.dto.portal.TareaCalendarioDTO;
import com.cesde.proyecto_academix.service.PortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints pensados para las pantallas de React (Academix-Front-Final).
 * Los CRUD originales (/api/usuarios, /api/materias, /api/tareas...) siguen igual.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PortalController {

    private final PortalService portalService;

    // Estudiante: "Mis notas"
    @GetMapping("/estudiantes/{id}/notas")
    public HijoDTO notasDeEstudiante(@PathVariable Long id) {
        return portalService.notasDeEstudiante(id);
    }

    // Acudiente: "Mis hijos" y "Detalle del hijo"
    @GetMapping("/acudientes/{id}/hijos")
    public List<HijoDTO> hijosDeAcudiente(@PathVariable Long id) {
        return portalService.hijosDeAcudiente(id);
    }

    @GetMapping("/hijos/{id}")
    public HijoDTO hijo(@PathVariable Long id) {
        return portalService.hijo(id);
    }

    // Docente: materias, estudiantes de una materia y guardado de notas
    @GetMapping("/docentes/{id}/materias")
    public List<MateriaDocenteDTO> materiasDeDocente(@PathVariable Long id) {
        return portalService.materiasDeDocente(id);
    }

    @GetMapping("/docente/materias/{materiaId}/estudiantes")
    public List<EstudianteNotasDTO> estudiantesDeMateria(@PathVariable Long materiaId) {
        return portalService.estudiantesDeMateria(materiaId);
    }

    @PutMapping("/docente/calificaciones")
    public ResultadoGuardadoDTO guardarCalificaciones(@RequestBody GuardarCalificacionesDTO dto) {
        return portalService.guardarCalificaciones(dto);
    }

    // Calendario
    @GetMapping("/calendario/tareas")
    public List<TareaCalendarioDTO> tareasCalendario() {
        return portalService.tareasCalendario();
    }

    @PostMapping("/calendario/tareas")
    public ResponseEntity<TareaCalendarioDTO> crearTareaCalendario(@RequestBody TareaCalendarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portalService.crearTareaCalendario(dto));
    }

    @GetMapping("/festivos")
    public List<FestivoDTO> festivos() {
        return portalService.festivos();
    }
}
