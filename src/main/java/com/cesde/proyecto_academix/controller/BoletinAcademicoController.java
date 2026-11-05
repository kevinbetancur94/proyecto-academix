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

import com.cesde.proyecto_academix.dto.BoletinAcademicoRequestDTO;
import com.cesde.proyecto_academix.dto.BoletinAcademicoResponseDTO;
import com.cesde.proyecto_academix.service.BoletinAcademicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador de boletines académicos.
 * Recibe BoletinAcademicoRequestDTO y responde BoletinAcademicoResponseDTO; los errores los maneja GlobalExceptionHandler.
 */
@Tag(name = "Boletines", description = "Gestión de boletines académicos (uno por estudiante)")
@RestController
@RequestMapping("/api/boletines")
@RequiredArgsConstructor
public class BoletinAcademicoController {

    private final BoletinAcademicoService boletinService;

    @Operation(summary = "Listar todos los registros de boletines")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public List<BoletinAcademicoResponseDTO> listar() {
        return boletinService.listarTodos();
    }

    @Operation(summary = "Obtener boletín por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BoletinAcademicoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(boletinService.obtenerPorId(id));
    }

    @Operation(summary = "Crear boletín")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Una entidad relacionada no existe")
    })
    @PostMapping
    public ResponseEntity<BoletinAcademicoResponseDTO> crear(@Valid @RequestBody BoletinAcademicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletinService.crear(dto));
    }

    @Operation(summary = "Actualizar boletín")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "El registro o una entidad relacionada no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BoletinAcademicoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody BoletinAcademicoRequestDTO dto) {
        return ResponseEntity.ok(boletinService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar boletín")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletinService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
