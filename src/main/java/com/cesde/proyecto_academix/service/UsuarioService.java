package com.cesde.proyecto_academix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.proyecto_academix.dto.ContactoDTO;
import com.cesde.proyecto_academix.dto.UsuarioRequestDTO;
import com.cesde.proyecto_academix.dto.UsuarioResponseDTO;
import com.cesde.proyecto_academix.exception.RecursoNoEncontradoException;
import com.cesde.proyecto_academix.exception.ReglaDeNegocioException;
import com.cesde.proyecto_academix.model.embeddable.Contacto;
import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        return convertirADTO(obtenerEntidadPorId(id));
    }

    /** Uso interno entre servicios: devuelve la ENTIDAD (nunca se expone a los controladores). */
    public Usuario obtenerEntidadPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id " + id));
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        validarCorreoUnico(dto.correo(), null);

        Usuario usuario = new Usuario();
        copiarDatos(dto, usuario);
        validarGradoSegunRol(usuario);

        return convertirADTO(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = obtenerEntidadPorId(id);

        validarCorreoUnico(dto.correo(), id);
        copiarDatos(dto, usuario);
        validarGradoSegunRol(usuario);

        return convertirADTO(usuarioRepository.save(usuario));
    }

    public void eliminar(Long id) {
        usuarioRepository.delete(obtenerEntidadPorId(id));
    }

    // Regla de negocio 1: el correo debe ser único en todo el sistema.
    private void validarCorreoUnico(String correo, Long idActual) {
        usuarioRepository.findByCorreo(correo).ifPresent(existente -> {
            if (idActual == null || !existente.getId().equals(idActual)) {
                throw new ReglaDeNegocioException("Ya existe un usuario registrado con el correo " + correo);
            }
        });
    }

    // Regla de negocio 2: solo los usuarios con rol ESTUDIANTE deben tener grado asignado.
    private void validarGradoSegunRol(Usuario usuario) {
        boolean esEstudiante = usuario.getRol() == RolUsuario.ESTUDIANTE;

        if (esEstudiante && (usuario.getGrado() == null || usuario.getGrado().isBlank())) {
            throw new ReglaDeNegocioException("Un usuario con rol ESTUDIANTE debe tener un grado asignado");
        }
        if (!esEstudiante && usuario.getGrado() != null && !usuario.getGrado().isBlank()) {
            throw new ReglaDeNegocioException("Solo los usuarios con rol ESTUDIANTE pueden tener grado asignado");
        }
    }

    // DTO de entrada -> Entidad (copia solo los campos que el cliente puede enviar)
    private void copiarDatos(UsuarioRequestDTO dto, Usuario usuario) {
        usuario.setNombre(dto.nombre());
        usuario.setCorreo(dto.correo());
        usuario.setPassword(dto.password());
        usuario.setRol(dto.rol());
        usuario.setGrado(dto.grado());
        ContactoDTO c = dto.contacto();
        usuario.setContacto(c == null ? null : new Contacto(c.telefono(), c.departamento(), c.ciudad()));
    }

    // Entidad -> DTO de salida (NO copia el password)
    private UsuarioResponseDTO convertirADTO(Usuario u) {
        Contacto c = u.getContacto();
        ContactoDTO contacto = c == null ? null : new ContactoDTO(c.getTelefono(), c.getDepartamento(), c.getCiudad());
        return new UsuarioResponseDTO(u.getId(), u.getNombre(), u.getCorreo(), u.getRol(), u.getGrado(),
                contacto, u.getEstadoActivo(), u.getFechaCreacion());
    }
}
