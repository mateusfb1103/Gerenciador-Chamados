package com.example.chamado.user.service;

import com.example.chamado.user.dto.*;
import com.example.chamado.user.model.Role;
import com.example.chamado.user.model.User;
import com.example.chamado.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO criar(UserCreateDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        User user = User.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(dto.getRole() != null ? dto.getRole() : Role.ROLE_USER)
                .build();

        userRepository.save(user);

        return toDTO(user);
    }

    public List<UserResponseDTO> listarTodos() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public UserResponseDTO buscarPorId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toDTO(user);
    }

    public UserResponseDTO atualizar(Long id, UserUpdateDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.getNome() != null) user.setNome(dto.getNome());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getSenha() != null) user.setSenha(passwordEncoder.encode(dto.getSenha()));
        if (dto.getRole() != null) user.setRole(dto.getRole());

        userRepository.save(user);

        return toDTO(user);
    }

    public void deletar(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
