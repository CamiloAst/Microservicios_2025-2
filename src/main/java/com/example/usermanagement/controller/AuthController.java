package com.example.usermanagement.controller;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Crea una cuenta con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario registrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponse.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "409", description = "El usuario ya existe", content = @Content)
    })
    public UserResponse register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de registro del usuario",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterRequest.class)
            )
        )
        @RequestBody RegisterRequest request
    ) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica al usuario y devuelve un token JWT"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(implementation = String.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    public String login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciales del usuario (username y password)",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class)
            )
        )
        @RequestBody LoginRequest request
    ) {
        return userService.login(request);
    }

    @PostMapping("/forgot")
    @Operation(
        summary = "Solicitar restablecimiento de contraseña",
        description = "Genera un token para restablecer la contraseña"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Solicitud aceptada",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(implementation = String.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    public String forgotPassword(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Correo electrónico del usuario",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ForgotPasswordRequest.class)
            )
        )
        @RequestBody ForgotPasswordRequest request
    ) {
        return userService.forgotPassword(request);
    }

    @PostMapping("/reset")
    @Operation(
        summary = "Restablecer contraseña",
        description = "Establece una nueva contraseña usando el token recibido"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contraseña restablecida", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Token inválido", content = @Content)
    })
    public void resetPassword(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Token de restablecimiento y nueva contraseña",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResetPasswordRequest.class)
            )
        )
        @RequestBody ResetPasswordRequest request
    ) {
        userService.resetPassword(request);
    }
}
