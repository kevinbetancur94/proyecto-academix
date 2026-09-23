
package com.cesde.proyecto_academix.service;

import com.cesde.proyecto_academix.model.entity.BoletinAcademico;
import com.cesde.proyecto_academix.repository.BoletinAcademicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoletinAcademicoService {

    private final BoletinAcademicoRepository boletinRepository;
    private final UsuarioService usuarioService; // así consultas Usuario sin tocar su repository

    public List<BoletinAcademico> listarTodos() {
        return boletinRepository.findAll();
    }

    public BoletinAcademico obtenerPorId(Long id) {
        return boletinRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe el boletín con id " + id));
    }

    public BoletinAcademico crear(BoletinAcademico b) {
        validarPromedioValido(b);
        validarEstudianteSinBoletinPrevio(b);
        return boletinRepository.save(b);
    }

    public BoletinAcademico actualizar(Long id, BoletinAcademico datos) {
        BoletinAcademico existente = obtenerPorId(id);
        existente.setPromedioGeneral(datos.getPromedioGeneral());
        existente.setMateriasAprobadas(datos.getMateriasAprobadas());
        existente.setMateriasEnRiesgo(datos.getMateriasEnRiesgo());
        validarPromedioValido(existente);
        return boletinRepository.save(existente);
    }

    public void eliminar(Long id) {
        boletinRepository.delete(obtenerPorId(id));
    }

    // Regla de negocio 1: el promedio general debe estar entre 0.0 y 5.0
    private void validarPromedioValido(BoletinAcademico b) {
        if (b.getPromedioGeneral() == null || b.getPromedioGeneral() < 0 || b.getPromedioGeneral() > 5) {
            throw new IllegalArgumentException("El promedio general debe estar entre 0 y 5");
        }
    }

    // Regla de negocio 2: un estudiante no puede tener más de un boletín (la relación es OneToOne)
    private void validarEstudianteSinBoletinPrevio(BoletinAcademico b) {
        // primero confirmamos que el estudiante exista de verdad
        usuarioService.obtenerPorId(b.getEstudiante().getId());

        boletinRepository.findByEstudianteId(b.getEstudiante().getId())
            .ifPresent(existe -> {
                throw new IllegalArgumentException("Este estudiante ya tiene un boletín académico registrado");
            });
    }
}