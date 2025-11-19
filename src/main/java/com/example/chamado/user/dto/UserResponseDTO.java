package com.example.chamado.user.dto;

import com.example.chamado.user.model.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Role role;
}
