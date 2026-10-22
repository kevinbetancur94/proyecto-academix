package com.cesde.proyecto_academix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.proyecto_academix.dto.BoletinAcademicoRequestDTO;
import com.cesde.proyecto_academix.dto.BoletinAcademicoResponseDTO;
import com.cesde.proyecto_academix.exception.RecursoNoEncontradoException;
import com.cesde.proyecto_academix.exception.ReglaDeNegocioException;
import com.cesde.proyecto_academix.model.entity.BoletinAcademico;
import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.repository.BoletinAcademicoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoletinAcademicoService {

    private final BoletinAcademicoRepository boletinRepository;
    private final UsuarioService usuarioService; // así consultamos Usuario sin tocar su repository

    public List<BoletinAcademicoResponseDTO> listarTodos() {
        return boletinRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public BoletinAcademicoResponseDTO obtenerPorId(Long id) {
        return convertirADTO(obtenerEntidadPorId(id));
    }

    private BoletinAcademico obtenerEntidadPorId(Long id) {
        return boletinRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el boletín con id " + id));
    }

    public BoletinAcademicoResponseDTO crear(BoletinAcademicoRequestDTO dto) {
        validarPromedioValido(dto.promedioGeneral());
        Usuario estudiante = validarEstudianteSinBoletinPrevio(dto.estudianteId());

        BoletinAcademico boletin = new BoletinAcademico();
        boletin.setEstudiante(estudiante);
        boletin.setPromedioGeneral(dto.promedioGeneral());
        boletin.setMateriasAprobadas(dto.materiasAprobadas());
        boletin.setMateriasEnRiesgo(dto.materiasEnRiesgo());

        return convertirADTO(boletinRepository.save(boletin));
    }

    // El estudiante de un boletín no se reasigna: solo se actualizan las métricas.
    public BoletinAcademicoResponseDTO actualizar(Long id, BoletinAcademicoRequestDTO dto) {
        BoletinAcademico existente = obtenerEntidadPorId(id);

        validarPromedioValido(dto.promedioGeneral());
        existente.setPromedioGeneral(dto.promedioGeneral());
        existente.setMateriasAprobadas(dto.materiasAprobadas());
        existente.setMateriasEnRiesgo(dto.materiasEnRiesgo());

        return convertirADTO(boletinRepository.save(existente));
    }

    public void eliminar(Long id) {
        boletinRepository.delete(obtenerEntidadPorId(id));
    }

    // Regla de negocio 1: el promedio general debe estar entre 0.0 y 5.0
    private void validarPromedioValido(Double promedio) {
        if (promedio == null || promedio < 0 || promedio > 5) {
            throw new ReglaDeNegocioException("El promedio general debe estar entre 0 y 5");
        }
    }

    // Regla de negocio 2: un estudiante no puede tener más de un boletín (la relación es OneToOne)
    private Usuario validarEstudianteSinBoletinPrevio(Long estudianteId) {
        if (estudianteId == null) {
            throw new ReglaDeNegocioException("El boletín debe estar asociado a un estudiante");
        }

        // primero confirmamos que el estudiante exista de verdad (404 si no)
        Usuario estudiante = usuarioService.obtenerEntidadPorId(estudianteId);

        boletinRepository.findByEstudianteId(estudianteId).ifPresent(existe -> {
            throw new ReglaDeNegocioException("Este estudiante ya tiene un boletín académico registrado");
        });
        return estudiante;
    }

    private BoletinAcademicoResponseDTO convertirADTO(BoletinAcademico b) {
        return new BoletinAcademicoResponseDTO(b.getId(),
                b.getEstudiante().getId(), b.getEstudiante().getNombre(),
                b.getPromedioGeneral(), b.getMateriasAprobadas(), b.getMateriasEnRiesgo());
    }
}
