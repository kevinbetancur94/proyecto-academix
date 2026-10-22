package com.cesde.proyecto_academix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.proyecto_academix.dto.MateriaRequestDTO;
import com.cesde.proyecto_academix.dto.MateriaResponseDTO;
import com.cesde.proyecto_academix.exception.RecursoNoEncontradoException;
import com.cesde.proyecto_academix.exception.ReglaDeNegocioException;
import com.cesde.proyecto_academix.model.entity.Materia;
import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final UsuarioService usuarioService;

    public List<MateriaResponseDTO> listarTodas() {
        return materiaRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public MateriaResponseDTO obtenerPorId(Long id) {
        return convertirADTO(obtenerEntidadPorId(id));
    }

    /** Uso interno entre servicios (p. ej. TareaService, CalificacionService): devuelve la ENTIDAD. */
    public Materia obtenerEntidadPorId(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una materia con el id " + id));
    }

    public MateriaResponseDTO crear(MateriaRequestDTO dto) {
        validarCodigoUnico(dto.codigo(), null);
        Usuario docente = validarDocente(dto);

        Materia materia = new Materia();
        materia.setNombre(dto.nombre());
        materia.setCodigo(dto.codigo());
        materia.setDocente(docente);

        return convertirADTO(materiaRepository.save(materia));
    }

    public MateriaResponseDTO actualizar(Long id, MateriaRequestDTO dto) {
        Materia materia = obtenerEntidadPorId(id);

        validarCodigoUnico(dto.codigo(), id);
        Usuario docente = validarDocente(dto);

        materia.setNombre(dto.nombre());
        materia.setCodigo(dto.codigo());
        materia.setDocente(docente);

        return convertirADTO(materiaRepository.save(materia));
    }

    public void eliminar(Long id) {
        materiaRepository.delete(obtenerEntidadPorId(id));
    }

    // Regla de negocio 1: el código de la materia debe ser único.
    private void validarCodigoUnico(String codigo, Long idActual) {
        materiaRepository.findByCodigo(codigo).ifPresent(existente -> {
            if (idActual == null || !existente.getId().equals(idActual)) {
                throw new ReglaDeNegocioException("Ya existe una materia registrada con el código " + codigo);
            }
        });
    }

    // Regla de negocio 2: el usuario asignado como docente debe tener rol DOCENTE.
    private Usuario validarDocente(MateriaRequestDTO dto) {
        if (dto.docenteId() == null) {
            throw new ReglaDeNegocioException("La materia debe tener un docente asignado");
        }

        Usuario docente = usuarioService.obtenerEntidadPorId(dto.docenteId());

        if (docente.getRol() != RolUsuario.DOCENTE) {
            throw new ReglaDeNegocioException("El usuario asignado como docente debe tener rol DOCENTE");
        }
        return docente;
    }

    private MateriaResponseDTO convertirADTO(Materia m) {
        return new MateriaResponseDTO(m.getId(), m.getNombre(), m.getCodigo(),
                m.getDocente().getId(), m.getDocente().getNombre(), m.getEstadoActivo());
    }
}
