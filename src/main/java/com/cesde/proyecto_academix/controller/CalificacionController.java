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
import lombok.RequiredArgsConstructor;

/**
 * Controlador de calificaciones.
 * Recibe CalificacionRequestDTO y responde CalificacionResponseDTO; los errores los maneja GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @GetMapping
    public List<CalificacionResponseDTO> listar() {
        return calificacionService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(calificacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> crear(@RequestBody CalificacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calificacionService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> actualizar(@PathVariable Long id, @RequestBody CalificacionRequestDTO dto) {
        return ResponseEntity.ok(calificacionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        calificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
