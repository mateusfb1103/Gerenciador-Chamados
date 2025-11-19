package com.example.chamado.auth.controller;

import com.example.chamado.auth.model.AuthRequest;
import com.example.chamado.auth.model.AuthResponse;
import com.example.chamado.auth.model.RegisterRequest;
import com.example.chamado.auth.service.AuthService;
import com.example.chamado.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Define a role default como USER se não for enviada
        if (request.getRole() == null || request.getRole().isBlank()) {
            request.setRole(Role.ROLE_USER.name());
        }

        return ResponseEntity.ok(authService.register(request));
    }
}
