package com.example.usermanagement.controller;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.events.UserLoginEvent;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.service.UserEventPublisher;
import com.example.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserEventPublisher userEventPublisher;
    @Autowired
    private UserRepository userRepository;

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

        @ApiResponse(
          responseCode = "401",
          description = "No autorizado",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{\"error\":\"Unauthorized\"}"))),

        @ApiResponse(
            responseCode = "400",
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
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
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no existe"));
        String login = userService.login(request);

        try {
            userEventPublisher.publish(
                    new UserLoginEvent(user.getUsername(), user.getEmail(), Instant.now().toString()));
        } catch (Exception e) {
            // log.warn("No se pudo publicar evento de login", e);
        }
        return login;
    }

    @PostMapping("/token")
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
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
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
}
