package com.example.usermanagement.controller;

import com.example.usermanagement.dto.RegisterRequest;
import com.example.usermanagement.dto.UserResponse;
import com.example.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista todos los usuarios", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios listados"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios")
    })
    public Page<UserResponse> listUsers(@Parameter(description = "Información de paginación") Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtiene un usuario por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public UserResponse getUser(@Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualiza un usuario existente", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public UserResponse updateUser(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a actualizar", required = true)
            @RequestBody RegisterRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Elimina un usuario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public void deleteUser(@Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        userService.deleteUser(id);
    }
}
