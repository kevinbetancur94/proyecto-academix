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
import lombok.RequiredArgsConstructor;

/**
 * Controlador de materias.
 * Recibe MateriaRequestDTO y responde MateriaResponseDTO; los errores los maneja GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    @GetMapping
    public List<MateriaResponseDTO> listar() {
        return materiaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(materiaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<MateriaResponseDTO> crear(@RequestBody MateriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaResponseDTO> actualizar(@PathVariable Long id, @RequestBody MateriaRequestDTO dto) {
        return ResponseEntity.ok(materiaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        materiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
