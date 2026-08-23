package com.cesde.proyectoacademix.model.entity;

import com.cesde.proyectoacademix.model.base.BaseEntity;
import com.cesde.proyectoacademix.model.embeddable.Contacto;
import com.cesde.proyectoacademix.model.enums.RolUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario extends BaseEntity {

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true, length = 120)
    private String correo;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private RolUsuario rol;

    // Solo aplica cuando rol = ESTUDIANTE. Se deja como String simple por ahora;
    // si el curso pide catálogo de grados, se separa en su propia entidad después.
    @Column(name = "grado", length = 10)
    private String grado;

    @Embedded
    private Contacto contacto;

    // Un acudiente puede tener varios estudiantes a cargo,
    // y un estudiante puede tener más de un acudiente (mamá, papá, etc.)
    @ManyToMany
    @JoinTable(
            name = "acudiente_estudiante",
            joinColumns = @JoinColumn(name = "acudiente_id"),
            inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    @Builder.Default
    private Set<Usuario> estudiantesACargo = new HashSet<>();

    @ManyToMany(mappedBy = "estudiantesACargo")
    @Builder.Default
    private Set<Usuario> acudientes = new HashSet<>();

    // Lado inverso de Materia.docente (@ManyToOne) -> completa el par @OneToMany/@ManyToOne
    @OneToMany(mappedBy = "docente")
    @Builder.Default
    private Set<Materia> materiasDictadas = new HashSet<>();
}
