package com.cesde.proyectoacademix.model.entity;

import com.cesde.proyectoacademix.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table; 
import lombok.AllArgsConstructor; 
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

/** 
 * Resumen académico calculado por estudiante (promedio general, * materias aprobadas y materias en riesgo). 
 * Se actualiza desde la * capa de servicio cada vez que se registra una nueva Calificacion. */

@Entity
@Table(name = "boletines_academicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BoletinAcademico extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "estudiante_id", nullable = false, unique = true)
    private Usuario estudiante;
    
    @Column(name = "promedio_general")
    private Double promedioGeneral; 
    
    @Column(name = "materias_aprobadas")
    private Integer materiasAprobadas; 
    
    @Column(name = "materias_en_riesgo")
    private Integer materiasEnRiesgo;

}