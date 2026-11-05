package com.cesde.proyecto_academix.controller;

import com.cesde.proyecto_academix.dto.portal.EstudianteNotasDTO;
import com.cesde.proyecto_academix.dto.portal.FestivoDTO;
import com.cesde.proyecto_academix.dto.portal.GuardarCalificacionesDTO;
import com.cesde.proyecto_academix.dto.portal.HijoDTO;
import com.cesde.proyecto_academix.dto.portal.MateriaDocenteDTO;
import com.cesde.proyecto_academix.dto.portal.ResultadoGuardadoDTO;
import com.cesde.proyecto_academix.dto.portal.TareaCalendarioDTO;
import com.cesde.proyecto_academix.service.PortalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Portal", description = "Endpoints para las pantallas del frontend: notas, hijos, docentes, calendario y festivos")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PortalController {

    private final PortalService portalService;

    // Estudiante: "Mis notas"
    @Operation(summary = "Notas de un estudiante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notas obtenidas"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/estudiantes/{id}/notas")
    public HijoDTO notasDeEstudiante(@PathVariable Long id) {
        return portalService.notasDeEstudiante(id);
    }

    // Acudiente: "Mis hijos" y "Detalle del hijo"
    @Operation(summary = "Hijos de un acudiente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de hijos"),
            @ApiResponse(responseCode = "404", description = "Acudiente no encontrado")
    })
    @GetMapping("/acudientes/{id}/hijos")
    public List<HijoDTO> hijosDeAcudiente(@PathVariable Long id) {
        return portalService.hijosDeAcudiente(id);
    }

    @Operation(summary = "Detalle de un hijo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle obtenido"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/hijos/{id}")
    public HijoDTO hijo(@PathVariable Long id) {
        return portalService.hijo(id);
    }

    // Docente: materias, estudiantes de una materia y guardado de notas
    @Operation(summary = "Materias de un docente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materias obtenidas"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @GetMapping("/docentes/{id}/materias")
    public List<MateriaDocenteDTO> materiasDeDocente(@PathVariable Long id) {
        return portalService.materiasDeDocente(id);
    }

    @Operation(summary = "Estudiantes y notas de una materia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estudiantes obtenidos"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @GetMapping("/docente/materias/{materiaId}/estudiantes")
    public List<EstudianteNotasDTO> estudiantesDeMateria(@PathVariable Long materiaId) {
        return portalService.estudiantesDeMateria(materiaId);
    }

    @Operation(summary = "Guardar calificaciones de una materia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Calificaciones guardadas"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Materia o estudiante no encontrado")
    })
    @PutMapping("/docente/calificaciones")
    public ResultadoGuardadoDTO guardarCalificaciones(@Valid @RequestBody GuardarCalificacionesDTO dto) {
        return portalService.guardarCalificaciones(dto);
    }

    // Calendario
    @Operation(summary = "Listar tareas del calendario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tareas obtenidas")
    })
    @GetMapping("/calendario/tareas")
    public List<TareaCalendarioDTO> tareasCalendario() {
        return portalService.tareasCalendario();
    }

    @Operation(summary = "Crear tarea en el calendario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarea creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o fecha anterior a hoy"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @PostMapping("/calendario/tareas")
    public ResponseEntity<TareaCalendarioDTO> crearTareaCalendario(@Valid @RequestBody TareaCalendarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portalService.crearTareaCalendario(dto));
    }

    @Operation(summary = "Listar festivos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Festivos obtenidos")
    })
    @GetMapping("/festivos")
    public List<FestivoDTO> festivos() {
        return portalService.festivos();
    }
}
