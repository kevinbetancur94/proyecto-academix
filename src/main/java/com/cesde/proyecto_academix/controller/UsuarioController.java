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

import com.cesde.proyecto_academix.dto.UsuarioRequestDTO;
import com.cesde.proyecto_academix.dto.UsuarioResponseDTO;
import com.cesde.proyecto_academix.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador de usuarios.
 * Recibe UsuarioRequestDTO y responde UsuarioResponseDTO; los errores los maneja GlobalExceptionHandler.
 */
@Tag(name = "Usuarios", description = "Gestión de usuarios (estudiantes, acudientes y docentes)")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar todos los registros de usuarios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listarTodos();
    }

    @Operation(summary = "Obtener usuario por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @Operation(summary = "Crear usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Una entidad relacionada no existe")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @Operation(summary = "Actualizar usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "El registro o una entidad relacionada no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un registro con ese id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
