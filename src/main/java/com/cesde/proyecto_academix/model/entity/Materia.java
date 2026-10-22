package com.cesde.proyecto_academix.model.entity;

import com.cesde.proyecto_academix.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "materias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia extends BaseEntity {

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "docente_id", nullable = false)
    private Usuario docente;

    // Lado inverso de Calificacion.materia (@ManyToOne)
    @OneToMany(mappedBy = "materia")
    @Builder.Default
    private Set<Calificacion> calificaciones = new HashSet<>();

    // Lado inverso de Tarea.materia (@ManyToOne)
    @OneToMany(mappedBy = "materia")
    @Builder.Default
    private Set<Tarea> tareas = new HashSet<>();
}

