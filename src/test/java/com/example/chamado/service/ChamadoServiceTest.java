package com.example.chamado.service;

import com.example.chamado.chamados.dto.ChamadoCreateDTO;
import com.example.chamado.chamados.dto.ChamadoResponseDTO;
import com.example.chamado.chamados.model.Chamado;
import com.example.chamado.chamados.model.ChamadoPrioridade;
import com.example.chamado.chamados.model.ChamadoStatus;
import com.example.chamado.chamados.repository.ChamadoRepository;
import com.example.chamado.chamados.service.ChamadoService;
import com.example.chamado.user.model.User;
import com.example.chamado.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChamadoService chamadoService;

    @Test
    @DisplayName("Deve criar chamado com sucesso para um usuário existente")
    void criarChamadoSucesso() {
        // Arrange
        Long userId = 1L;
        ChamadoCreateDTO dto = new ChamadoCreateDTO();
        dto.setTitulo("Internet Lenta");
        dto.setDescricao("Não consigo acessar o Google");
        dto.setPrioridade(ChamadoPrioridade.ALTA);

        User user = User.builder().id(userId).email("user@test.com").build();

        Chamado chamadoSalvo = Chamado.builder()
                .id(10L)
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .status(ChamadoStatus.ABERTO)
                .prioridade(dto.getPrioridade())
                .usuario(user)
                .criadoEm(LocalDateTime.now())
                .build();

        // Mocks
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(chamadoSalvo);

        // Act
        ChamadoResponseDTO result = chamadoService.criarChamado(userId, dto);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Internet Lenta", result.getTitulo());
        assertEquals("user@test.com", result.getUsuarioEmail());
        assertEquals(ChamadoStatus.ABERTO, result.getStatus());
    }

    @Test
    @DisplayName("Deve lançar erro ao criar chamado para usuário inexistente")
    void criarChamadoUsuarioNaoEncontrado() {
        Long userId = 99L;
        ChamadoCreateDTO dto = new ChamadoCreateDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            chamadoService.criarChamado(userId, dto);
        });

        verify(chamadoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar chamados de um usuário")
    void listarChamadosDoUsuario() {
        Long userId = 1L;
        User user = User.builder().id(userId).email("a@a.com").build();

        Chamado c1 = Chamado.builder().id(1L).titulo("C1").usuario(user).build();
        Chamado c2 = Chamado.builder().id(2L).titulo("C2").usuario(user).build();

        when(chamadoRepository.findByUsuarioId(userId)).thenReturn(List.of(c1, c2));

        List<ChamadoResponseDTO> result = chamadoService.listarChamadosDoUsuario(userId);

        assertEquals(2, result.size());
        assertEquals("C1", result.get(0).getTitulo());
    }
}