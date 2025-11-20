package com.example.chamado.chamados.controller;

import com.example.chamado.chamados.dto.ChamadoCreateDTO;
import com.example.chamado.chamados.dto.ChamadoResponseDTO;
import com.example.chamado.chamados.dto.ChamadoUpdateDTO;
import com.example.chamado.chamados.service.ChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

    @PostMapping("/{userId}")
    public ResponseEntity<ChamadoResponseDTO> criar(
            @PathVariable Long userId,
            @RequestBody ChamadoCreateDTO dto
    ) {
        return ResponseEntity.ok(chamadoService.criarChamado(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChamadoResponseDTO>> listar(@PathVariable Long userId) {
        return ResponseEntity.ok(chamadoService.listarChamadosDoUsuario(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ChamadoUpdateDTO dto
    ) {
        return ResponseEntity.ok(chamadoService.atualizarChamado(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        chamadoService.deletarChamado(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(chamadoService.listarTodos());
    }
}
