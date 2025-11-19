package com.example.chamado.chamados.dto;

import com.example.chamado.chamados.model.ChamadoPrioridade;
import com.example.chamado.chamados.model.ChamadoStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChamadoResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private ChamadoStatus status;
    private ChamadoPrioridade prioridade;
    private String usuarioEmail;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
