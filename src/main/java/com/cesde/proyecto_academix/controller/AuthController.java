package com.cesde.proyecto_academix.controller;

import com.cesde.proyecto_academix.dto.portal.LoginRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RecuperarRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RegistroRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RespuestaOkDTO;
import com.cesde.proyecto_academix.dto.portal.SesionDTO;
import com.cesde.proyecto_academix.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Login, registro y recuperación de contraseña que usa el frontend de React. */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SesionDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.iniciarSesion(dto));
    }

    @PostMapping("/registro")
    public ResponseEntity<RespuestaOkDTO> registro(@RequestBody RegistroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(dto));
    }

    @PostMapping("/recuperar")
    public ResponseEntity<RespuestaOkDTO> recuperar(@RequestBody RecuperarRequestDTO dto) {
        return ResponseEntity.ok(authService.recuperar(dto));
    }
}
