package com.cesde.proyectoacademix.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "departamento", length = 80)
    private String departamento;

    @Column(name = "ciudad", length = 80)
    private String ciudad;
}
