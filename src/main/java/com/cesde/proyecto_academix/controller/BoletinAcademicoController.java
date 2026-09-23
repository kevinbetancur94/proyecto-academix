
package com.cesde.proyecto_academix.controller;

import com.cesde.proyecto_academix.model.entity.BoletinAcademico;
import com.cesde.proyecto_academix.service.BoletinAcademicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/boletines")
@RequiredArgsConstructor
public class BoletinAcademicoController {

    private final BoletinAcademicoService boletinService;

    @GetMapping
    public List<BoletinAcademico> listar() {
        return boletinService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(boletinService.obtenerPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody BoletinAcademico b) {
        try {
            return ResponseEntity.ok(boletinService.crear(b));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody BoletinAcademico datos) {
        try {
            return ResponseEntity.ok(boletinService.actualizar(id, datos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            boletinService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}