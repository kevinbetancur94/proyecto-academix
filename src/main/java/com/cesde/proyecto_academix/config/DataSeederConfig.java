package com.cesde.proyecto_academix.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cesde.proyecto_academix.dto.MateriaRequestDTO;
import com.cesde.proyecto_academix.dto.TareaRequestDTO;
import com.cesde.proyecto_academix.dto.UsuarioRequestDTO;
import com.cesde.proyecto_academix.dto.UsuarioResponseDTO;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.model.enums.TipoTarea;
import com.cesde.proyecto_academix.repository.UsuarioRepository;
import com.cesde.proyecto_academix.service.MateriaService;
import com.cesde.proyecto_academix.service.TareaService;
import com.cesde.proyecto_academix.service.UsuarioService;

import lombok.RequiredArgsConstructor;

/**
 * Carga datos de ejemplo para la demostración. Es opcional: solo corre si SEED_DATA=true
 * (en .env o variable de entorno) y únicamente cuando la tabla de usuarios está vacía.
 */
@Configuration
@RequiredArgsConstructor
public class DataSeederConfig {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final MateriaService materiaService;
    private final TareaService tareaService;

    @Bean
    @ConditionalOnProperty(name = "SEED_DATA", havingValue = "true")
    CommandLineRunner cargarDatosDeEjemplo() {
        return args -> {
            if (usuarioRepository.count() > 0) {
                return;
            }
            UsuarioResponseDTO docente = usuarioService.crear(
                    new UsuarioRequestDTO("Laura Gómez", "laura.docente@academix.com", "docente123", RolUsuario.DOCENTE, null, null));
            usuarioService.crear(
                    new UsuarioRequestDTO("Carlos Pérez", "carlos.estudiante@academix.com", "estudiante123", RolUsuario.ESTUDIANTE, "10A", null));
            usuarioService.crear(
                    new UsuarioRequestDTO("Marta Ruiz", "marta.acudiente@academix.com", "acudiente123", RolUsuario.ACUDIENTE, null, null));

            var matematicas = materiaService.crear(new MateriaRequestDTO("Matemáticas", "MAT-101", docente.id()));
            tareaService.crear(new TareaRequestDTO("Taller de álgebra", "Ejercicios 1 al 10", matematicas.id(),
                    TipoTarea.TAREA, "10A", LocalDate.now().plusDays(7), null));
        };
    }
}
