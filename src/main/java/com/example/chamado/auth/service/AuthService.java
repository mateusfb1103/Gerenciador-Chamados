package com.example.chamado.auth.service;

import com.example.chamado.auth.model.AuthRequest;
import com.example.chamado.auth.model.AuthResponse;
import com.example.chamado.auth.model.RegisterRequest;
import com.example.chamado.config.JwtService;
import com.example.chamado.user.model.Role;
import com.example.chamado.user.model.User;
import com.example.chamado.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Define ROLE_USER como padrão
        Role role = Role.ROLE_USER;

        // Se a role foi informada na requisição, tenta usar ela
        if (request.getRole() != null && !request.getRole().isBlank()) {
            try {
                // Tenta converter a String (ex: "ROLE_SUPPORT") para o Enum
                role = Role.valueOf(request.getRole());
            } catch (IllegalArgumentException e) {
                // Se a role enviada não existir no Enum, mantém o padrão ROLE_USER
                role = Role.ROLE_USER;
            }
        }

        User user = User.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(role) // Usa a role determinada
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}