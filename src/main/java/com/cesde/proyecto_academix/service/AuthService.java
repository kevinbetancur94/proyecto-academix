package com.cesde.proyecto_academix.service;

import com.cesde.proyecto_academix.dto.ContactoDTO;
import com.cesde.proyecto_academix.dto.UsuarioRequestDTO;
import com.cesde.proyecto_academix.dto.portal.LoginRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RecuperarRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RegistroRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RespuestaOkDTO;
import com.cesde.proyecto_academix.dto.portal.SesionDTO;
import com.cesde.proyecto_academix.exception.ReglaDeNegocioException;
import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import com.cesde.proyecto_academix.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Inicio de sesión y registro para el frontend de React.
 * NOTA: es un login académico (sin tokens ni contraseñas cifradas); el frontend
 * guarda la sesión que le devuelve este servicio.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public SesionDTO iniciarSesion(LoginRequestDTO dto) {
        String correo = dto.usuario() == null ? "" : dto.usuario().trim();

        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                .filter(u -> u.getPassword().equals(dto.password()))
                .orElseThrow(() -> new ReglaDeNegocioException("Correo o contraseña incorrectos"));

        if (!Boolean.TRUE.equals(usuario.getEstadoActivo())) {
            throw new ReglaDeNegocioException("Esta cuenta está inactiva");
        }

        // Regla de negocio: cada cuenta tiene UN rol; no se puede entrar con el rol de otra persona.
        String rolCuenta = usuario.getRol().name().toLowerCase(Locale.ROOT);
        if (dto.rol() != null && !rolCuenta.equalsIgnoreCase(dto.rol())) {
            throw new ReglaDeNegocioException("Esta cuenta es de " + nombreRol(usuario.getRol())
                    + ". Selecciona el rol correcto.");
        }

        Long estudianteId = usuario.getRol() == RolUsuario.ESTUDIANTE ? usuario.getId() : null;
        return new SesionDTO(usuario.getId(), usuario.getNombre(), usuario.getCorreo(),
                rolCuenta, usuario.getGrado(), estudianteId);
    }

    public RespuestaOkDTO registrar(RegistroRequestDTO dto) {
        if (dto.confirmar() != null && !dto.confirmar().equals(dto.password())) {
            throw new ReglaDeNegocioException("Las contraseñas no coinciden");
        }

        RolUsuario rol = convertirRol(dto.rol());
        String grado = rol == RolUsuario.ESTUDIANTE ? dto.grado() : null;
        String correo = dto.correo() == null ? null : dto.correo().trim().toLowerCase(Locale.ROOT);
        ContactoDTO contacto = new ContactoDTO(dto.telefono(), dto.departamento(), dto.ciudad());

        // Se reutiliza UsuarioService para aplicar sus reglas (correo único, grado solo para estudiantes)
        usuarioService.crear(new UsuarioRequestDTO(dto.nombre(), correo, dto.password(), rol, grado, contacto));
        return new RespuestaOkDTO(true, "Usuario " + dto.nombre() + " registrado");
    }

    // No hay servidor de correo: se responde OK para que la pantalla muestre su mensaje.
    public RespuestaOkDTO recuperar(RecuperarRequestDTO dto) {
        return new RespuestaOkDTO(true, "Si el correo existe, se enviarán instrucciones");
    }

    // Desde la página web solo se pueden registrar estudiantes, docentes y acudientes (no ADMIN).
    private RolUsuario convertirRol(String texto) {
        if (texto == null) {
            throw new ReglaDeNegocioException("Debe seleccionar un rol");
        }
        return switch (texto.trim().toLowerCase(Locale.ROOT)) {
            case "estudiante" -> RolUsuario.ESTUDIANTE;
            case "docente" -> RolUsuario.DOCENTE;
            case "acudiente" -> RolUsuario.ACUDIENTE;
            default -> throw new ReglaDeNegocioException("Rol no válido: " + texto);
        };
    }

    private String nombreRol(RolUsuario rol) {
        return switch (rol) {
            case ESTUDIANTE -> "Estudiante";
            case DOCENTE -> "Docente";
            case ACUDIENTE -> "Acudiente";
            case ADMIN -> "Administrador";
        };
    }
}
