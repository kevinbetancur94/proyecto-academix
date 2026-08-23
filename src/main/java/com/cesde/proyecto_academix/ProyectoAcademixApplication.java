package com.cesde.proyecto_academix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProyectoAcademixApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoAcademixApplication.class, args);
	}

}
