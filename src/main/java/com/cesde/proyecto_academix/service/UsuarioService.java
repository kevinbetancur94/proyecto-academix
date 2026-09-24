package com.cesde.proyecto_academix.service;

import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con el id " + id));
    }

    public Usuario crear(Usuario usuario) {
        validarCorreoUnico(usuario.getCorreo(), null);
        validarGradoSegunRol(usuario);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {
        Usuario usuario = obtenerPorId(id);

        validarCorreoUnico(datos.getCorreo(), id);

        usuario.setNombre(datos.getNombre());
        usuario.setCorreo(datos.getCorreo());
        usuario.setPassword(datos.getPassword());
        usuario.setRol(datos.getRol());
        usuario.setGrado(datos.getGrado());
        usuario.setContacto(datos.getContacto());

        validarGradoSegunRol(usuario);

        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    // Regla de negocio 1: el correo debe ser único en todo el sistema.
    private void validarCorreoUnico(String correo, Long idActual) {
        usuarioRepository.findByCorreo(correo).ifPresent(existente -> {
            if (idActual == null || !existente.getId().equals(idActual)) {
                throw new IllegalArgumentException("Ya existe un usuario registrado con el correo " + correo);
            }
        });
    }

    // Regla de negocio 2: solo los usuarios con rol ESTUDIANTE deben tener grado asignado.
    private void validarGradoSegunRol(Usuario usuario) {
        boolean esEstudiante = usuario.getRol() == RolUsuario.ESTUDIANTE;

        if (esEstudiante && (usuario.getGrado() == null || usuario.getGrado().isBlank())) {
            throw new IllegalArgumentException("Un usuario con rol ESTUDIANTE debe tener un grado asignado");
        }

        if (!esEstudiante && usuario.getGrado() != null && !usuario.getGrado().isBlank()) {
            throw new IllegalArgumentException("Solo los usuarios con rol ESTUDIANTE pueden tener grado asignado");
        }
    }
}
