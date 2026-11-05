package com.cesde.proyecto_academix.controller;

import com.cesde.proyecto_academix.dto.portal.LoginRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RecuperarRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RegistroRequestDTO;
import com.cesde.proyecto_academix.dto.portal.RespuestaOkDTO;
import com.cesde.proyecto_academix.dto.portal.SesionDTO;
import com.cesde.proyecto_academix.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Login, registro y recuperación de contraseña que usa el frontend de React. */
@Tag(name = "Autenticación", description = "Inicio de sesión, registro y recuperación de contraseña")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Iniciar sesión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión iniciada"),
            @ApiResponse(responseCode = "400", description = "Credenciales o datos inválidos")
    })
    @PostMapping("/login")
    public ResponseEntity<SesionDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.iniciarSesion(dto));
    }

    @Operation(summary = "Registrar un usuario nuevo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o correo ya registrado")
    })
    @PostMapping("/registro")
    public ResponseEntity<RespuestaOkDTO> registro(@Valid @RequestBody RegistroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(dto));
    }

    @Operation(summary = "Solicitar recuperación de contraseña")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud procesada"),
            @ApiResponse(responseCode = "400", description = "Correo inválido")
    })
    @PostMapping("/recuperar")
    public ResponseEntity<RespuestaOkDTO> recuperar(@Valid @RequestBody RecuperarRequestDTO dto) {
        return ResponseEntity.ok(authService.recuperar(dto));
    }
}
