package com.cesde.proyecto_academix.service;

import com.cesde.proyecto_academix.model.entity.Materia;
import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final UsuarioService usuarioService;

    public List<Materia> listarTodas() {
        return materiaRepository.findAll();
    }

    public Materia obtenerPorId(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una materia con el id " + id));
    }

    public Materia crear(Materia materia) {
        validarCodigoUnico(materia.getCodigo(), null);
        Usuario docente = validarDocente(materia);
        materia.setDocente(docente);
        return materiaRepository.save(materia);
    }

    public Materia actualizar(Long id, Materia datos) {
        Materia materia = obtenerPorId(id);

        validarCodigoUnico(datos.getCodigo(), id);
        Usuario docente = validarDocente(datos);

        materia.setNombre(datos.getNombre());
        materia.setCodigo(datos.getCodigo());
        materia.setDocente(docente);

        return materiaRepository.save(materia);
    }

    public void eliminar(Long id) {
        Materia materia = obtenerPorId(id);
        materiaRepository.delete(materia);
    }

    // Regla de negocio 1: el código de la materia debe ser único.
    private void validarCodigoUnico(String codigo, Long idActual) {
        materiaRepository.findByCodigo(codigo).ifPresent(existente -> {
            if (idActual == null || !existente.getId().equals(idActual)) {
                throw new IllegalArgumentException("Ya existe una materia registrada con el código " + codigo);
            }
        });
    }

    // Regla de negocio 2: el usuario asignado como docente debe tener rol DOCENTE.
    private Usuario validarDocente(Materia materia) {
        if (materia.getDocente() == null || materia.getDocente().getId() == null) {
            throw new IllegalArgumentException("La materia debe tener un docente asignado");
        }

        Usuario docente = usuarioService.obtenerPorId(materia.getDocente().getId());

        if (docente.getRol() != RolUsuario.DOCENTE) {
            throw new IllegalArgumentException("El usuario asignado como docente debe tener rol DOCENTE");
        }

        return docente;
    }
}
