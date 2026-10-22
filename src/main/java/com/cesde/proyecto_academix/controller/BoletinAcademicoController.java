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
import lombok.RequiredArgsConstructor;

/**
 * Controlador de boletines académicos.
 * Recibe BoletinAcademicoRequestDTO y responde BoletinAcademicoResponseDTO; los errores los maneja GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/boletines")
@RequiredArgsConstructor
public class BoletinAcademicoController {

    private final BoletinAcademicoService boletinService;

    @GetMapping
    public List<BoletinAcademicoResponseDTO> listar() {
        return boletinService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletinAcademicoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(boletinService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<BoletinAcademicoResponseDTO> crear(@RequestBody BoletinAcademicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletinService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoletinAcademicoResponseDTO> actualizar(@PathVariable Long id, @RequestBody BoletinAcademicoRequestDTO dto) {
        return ResponseEntity.ok(boletinService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletinService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
