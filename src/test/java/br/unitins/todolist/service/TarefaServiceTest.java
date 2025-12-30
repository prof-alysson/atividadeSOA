package br.unitins.todolist.service;

import br.unitins.todolist.dto.TarefaDTO;
import br.unitins.todolist.exception.ResourceNotFoundException;
import br.unitins.todolist.model.Tarefa;
import br.unitins.todolist.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para TarefaService
 *
 * Boas práticas aplicadas:
 * - Uso de Mocks para isolar dependências
 * - Testes independentes
 * - Nomenclatura descritiva
 * - Cobertura de casos de sucesso e falha
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private Tarefa tarefaMock;
    private TarefaDTO tarefaDtoMock;

    @BeforeEach
    void setUp() {
        tarefaMock = new Tarefa();
        tarefaMock.setId(1L);
        tarefaMock.setTitulo("Teste");
        tarefaMock.setDescricao("Descrição de teste");
        tarefaMock.setConcluida(false);

        tarefaDtoMock = new TarefaDTO();
        tarefaDtoMock.setTitulo("Teste");
        tarefaDtoMock.setDescricao("Descrição de teste");
        tarefaDtoMock.setConcluida(false);
    }

    @Test
    void deveCriarTarefaComSucesso() {
        // Given
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaMock);

        // When
        Tarefa resultado = tarefaService.criar(tarefaDtoMock);

        // Then
        assertNotNull(resultado);
        assertEquals("Teste", resultado.getTitulo());
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveListarTodasAsTarefas() {
        // Given
        List<Tarefa> tarefasMock = Arrays.asList(tarefaMock);
        when(tarefaRepository.findAllByOrderByCriadoEmDesc()).thenReturn(tarefasMock);

        // When
        List<Tarefa> resultado = tarefaService.listarTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(tarefaRepository, times(1)).findAllByOrderByCriadoEmDesc();
    }

    @Test
    void deveBuscarTarefaPorId() {
        // Given
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaMock));

        // When
        Tarefa resultado = tarefaService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(tarefaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        // Given
        when(tarefaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            tarefaService.buscarPorId(999L);
        });
        verify(tarefaRepository, times(1)).findById(999L);
    }

    @Test
    void deveAlternarStatusDeConclusao() {
        // Given
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaMock));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaMock);

        // When
        Tarefa resultado = tarefaService.toggleConcluida(1L);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.getConcluida()); // Status foi alternado
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveAtualizarTarefaComSucesso() {
        // Given
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaMock));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaMock);

        TarefaDTO updateDto = new TarefaDTO();
        updateDto.setTitulo("Título Atualizado");
        updateDto.setDescricao("Descrição Atualizada");
        updateDto.setConcluida(true);

        // When
        Tarefa resultado = tarefaService.atualizar(1L, updateDto);

        // Then
        assertNotNull(resultado);
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveDeletarTarefaComSucesso() {
        // Given
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaMock));
        doNothing().when(tarefaRepository).delete(any(Tarefa.class));

        // When
        tarefaService.deletar(1L);

        // Then
        verify(tarefaRepository, times(1)).delete(any(Tarefa.class));
    }

    @Test
    void deveListarTarefasPorStatus() {
        // Given
        List<Tarefa> tarefasMock = Arrays.asList(tarefaMock);
        when(tarefaRepository.findByConcluida(false)).thenReturn(tarefasMock);

        // When
        List<Tarefa> resultado = tarefaService.listarPorStatus(false);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(tarefaRepository, times(1)).findByConcluida(false);
    }

    @Test
    void deveBuscarTarefasPorTitulo() {
        // Given
        List<Tarefa> tarefasMock = Arrays.asList(tarefaMock);
        when(tarefaRepository.findByTituloContainingIgnoreCase("Teste")).thenReturn(tarefasMock);

        // When
        List<Tarefa> resultado = tarefaService.buscarPorTitulo("Teste");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Teste", resultado.get(0).getTitulo());
        verify(tarefaRepository, times(1)).findByTituloContainingIgnoreCase("Teste");
    }
}
