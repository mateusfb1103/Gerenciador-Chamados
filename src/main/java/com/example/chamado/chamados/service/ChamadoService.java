package com.example.chamado.chamados.service;

import com.example.chamado.chamados.dto.ChamadoCreateDTO;
import com.example.chamado.chamados.dto.ChamadoResponseDTO;
import com.example.chamado.chamados.dto.ChamadoUpdateDTO;
import com.example.chamado.chamados.model.Chamado;
import com.example.chamado.chamados.model.ChamadoStatus;
import com.example.chamado.chamados.repository.ChamadoRepository;
import com.example.chamado.exception.ResourceNotFoundException;
import com.example.chamado.user.model.User;
import com.example.chamado.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UserRepository userRepository;

    public ChamadoResponseDTO criarChamado(Long userId, ChamadoCreateDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Chamado chamado = Chamado.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .status(ChamadoStatus.ABERTO)
                .prioridade(dto.getPrioridade())
                .usuario(user)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        chamadoRepository.save(chamado);

        return toDTO(chamado);
    }

    public List<ChamadoResponseDTO> listarChamadosDoUsuario(Long userId) {
        return chamadoRepository.findByUsuarioId(userId)
                .stream().map(this::toDTO).toList();
    }

    public ChamadoResponseDTO atualizarChamado(Long id, ChamadoUpdateDTO dto) {

        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        if (dto.getTitulo() != null) chamado.setTitulo(dto.getTitulo());
        if (dto.getDescricao() != null) chamado.setDescricao(dto.getDescricao());
        if (dto.getPrioridade() != null) chamado.setPrioridade(dto.getPrioridade());
        if (dto.getStatus() != null) chamado.setStatus(dto.getStatus());

        chamado.setAtualizadoEm(LocalDateTime.now());

        chamadoRepository.save(chamado);

        return toDTO(chamado);
    }

    public void deletarChamado(Long id) {
        if (!chamadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Chamado não encontrado com o id: " + id);
        }
        chamadoRepository.deleteById(id);
    }

    private ChamadoResponseDTO toDTO(Chamado chamado) {
        return ChamadoResponseDTO.builder()
                .id(chamado.getId())
                .titulo(chamado.getTitulo())
                .descricao(chamado.getDescricao())
                .status(chamado.getStatus())
                .prioridade(chamado.getPrioridade())
                .usuarioEmail(chamado.getUsuario().getEmail())
                .criadoEm(chamado.getCriadoEm())
                .atualizadoEm(chamado.getAtualizadoEm())
                .build();
    }

    public List<ChamadoResponseDTO> listarTodos() {
        return chamadoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
