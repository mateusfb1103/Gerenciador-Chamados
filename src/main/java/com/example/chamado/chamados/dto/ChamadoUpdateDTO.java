package com.example.chamado.chamados.dto;

import com.example.chamado.chamados.model.ChamadoPrioridade;
import com.example.chamado.chamados.model.ChamadoStatus;
import lombok.Data;

@Data
public class ChamadoUpdateDTO {
    private String titulo;
    private String descricao;
    private ChamadoPrioridade prioridade;
    private ChamadoStatus status;
}
