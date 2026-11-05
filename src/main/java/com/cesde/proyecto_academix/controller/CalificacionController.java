package com.cesde.proyecto_academix.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.proyecto_academix.dto.CalificacionRequestDTO;
import com.cesde.proyecto_academix.dto.CalificacionResponseDTO;
import com.cesde.proyecto_academix.service.CalificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador de calificaciones.
 * Recibe CalificacionRequestDTO y responde CalificacionResponseDTO; los errores los maneja GlobalExceptionHandler.
 */
@Tag(name = "Calificaciones", description = "Gestión de calificaciones por estudiante y materia")
@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @Operation(summary = "Listar todos los registros de calificaciones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public List<CalificacionResponseDTO> listar() {
        return calificacionService.listarTodos();
    }

    @Operation(summary = "Obtener calificación por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(calificacionService.obtenerPorId(id));
    }

    @Operation(summary = "Crear calificación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Una entidad relacionada no existe")
    })
    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> crear(@Valid @RequestBody CalificacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calificacionService.crear(dto));
    }

    @Operation(summary = "Actualizar calificación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "El registro o una entidad relacionada no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CalificacionRequestDTO dto) {
        return ResponseEntity.ok(calificacionService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar calificación")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        calificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
