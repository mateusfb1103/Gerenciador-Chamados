package com.example.chamado.service;

import com.example.chamado.user.dto.UserCreateDTO;
import com.example.chamado.user.dto.UserResponseDTO;
import com.example.chamado.user.model.Role;
import com.example.chamado.user.model.User;
import com.example.chamado.user.repository.UserRepository;
import com.example.chamado.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void criarUsuarioSucesso() {
        // Cenário
        UserCreateDTO dto = new UserCreateDTO();
        dto.setNome("Teste Silva");
        dto.setEmail("teste@email.com");
        dto.setSenha("123456");
        dto.setRole(Role.ROLE_USER);

        User userSalvo = User.builder()
                .id(1L)
                .nome(dto.getNome())
                .email(dto.getEmail())
                .role(Role.ROLE_USER)
                .build();

        // Mocks
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("senhaCriptografada");
        when(userRepository.save(any(User.class))).thenReturn(userSalvo);

        // Ação (Act)
        UserResponseDTO result = userService.criar(dto);

        // Verificação (Assert)
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Teste Silva", result.getNome());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar usuário com email duplicado")
    void criarUsuarioEmailDuplicado() {
        // Cenário
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("duplicado@email.com");

        // Mock: Diz que o email já existe
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // Ação & Verificação
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.criar(dto);
        });

        assertEquals("Email já cadastrado.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void buscarPorIdSucesso() {
        User user = User.builder().id(1L).nome("Teste").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.buscarPorId(1L);

        assertEquals("Teste", result.getNome());
    }
}