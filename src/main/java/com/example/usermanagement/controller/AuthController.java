package com.example.usermanagement.controller;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.service.UserService;
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
    public UserResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/forgot")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return userService.forgotPassword(request);
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
    }
}
