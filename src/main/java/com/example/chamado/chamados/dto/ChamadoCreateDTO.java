package com.example.chamado.chamados.dto;

import com.example.chamado.chamados.model.ChamadoPrioridade;
import lombok.Data;

@Data
public class ChamadoCreateDTO {
    private String titulo;
    private String descricao;
    private ChamadoPrioridade prioridade;
}
