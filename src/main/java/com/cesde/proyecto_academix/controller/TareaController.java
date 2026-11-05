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

import com.cesde.proyecto_academix.dto.TareaRequestDTO;
import com.cesde.proyecto_academix.dto.TareaResponseDTO;
import com.cesde.proyecto_academix.service.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador de tareas.
 * Recibe TareaRequestDTO y responde TareaResponseDTO; los errores los maneja GlobalExceptionHandler.
 */
@Tag(name = "Tareas", description = "Gestión de tareas, talleres y evaluaciones")
@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @Operation(summary = "Listar todos los registros de tareas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public List<TareaResponseDTO> listar() {
        return tareaService.listarTodas();
    }

    @Operation(summary = "Obtener tarea por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerPorId(id));
    }

    @Operation(summary = "Crear tarea")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Una entidad relacionada no existe")
    })
    @PostMapping
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crear(dto));
    }

    @Operation(summary = "Actualizar tarea")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "El registro o una entidad relacionada no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TareaRequestDTO dto) {
        return ResponseEntity.ok(tareaService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar tarea")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
