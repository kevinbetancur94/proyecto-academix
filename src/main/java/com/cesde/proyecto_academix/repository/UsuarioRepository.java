package com.cesde.proyecto_academix.repository;

import com.cesde.proyecto_academix.model.entity.Usuario;
import com.cesde.proyecto_academix.model.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    // Login: el correo se compara sin importar mayúsculas/minúsculas
    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    // Panel docente: lista de estudiantes ordenada por nombre
    List<Usuario> findByRolOrderByNombreAsc(RolUsuario rol);
}
