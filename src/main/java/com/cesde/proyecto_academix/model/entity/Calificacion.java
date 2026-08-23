package com.cesde.proyecto_academix.model.entity;

import com.cesde.proyecto_academix.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calificacion extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @Column(name = "actividad_1", nullable = false)
    private Double act1;

    @Column(name = "actividad_2", nullable = false)
    private Double act2;

    @Column(name = "actividad_3", nullable = false)
    private Double act3;

    @Column(name = "observacion", length = 300)
    private String observacion;
}
