package com.example.chamado.user.dto;

import com.example.chamado.user.model.Role;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String nome;
    private String email;
    private String senha;
    private Role role;
}
