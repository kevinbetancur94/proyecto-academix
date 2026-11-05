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

import com.cesde.proyecto_academix.dto.MateriaRequestDTO;
import com.cesde.proyecto_academix.dto.MateriaResponseDTO;
import com.cesde.proyecto_academix.service.MateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador de materias.
 * Recibe MateriaRequestDTO y responde MateriaResponseDTO; los errores los maneja GlobalExceptionHandler.
 */
@Tag(name = "Materias", description = "Gestión de materias y su docente asignado")
@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    @Operation(summary = "Listar todos los registros de materias")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public List<MateriaResponseDTO> listar() {
        return materiaService.listarTodas();
    }

    @Operation(summary = "Obtener materia por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MateriaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(materiaService.obtenerPorId(id));
    }

    @Operation(summary = "Crear materia")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Una entidad relacionada no existe")
    })
    @PostMapping
    public ResponseEntity<MateriaResponseDTO> crear(@Valid @RequestBody MateriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.crear(dto));
    }

    @Operation(summary = "Actualizar materia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "El registro o una entidad relacionada no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MateriaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MateriaRequestDTO dto) {
        return ResponseEntity.ok(materiaService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar materia")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        materiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
