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
    private TarefaDTO todoDtoMock;

    @BeforeEach
    void setUp() {
        todoMock = new Todo();
        todoMock.setId(1L);
        todoMock.setTitulo("Teste");
        todoMock.setDescricao("Descrição de teste");
        todoMock.setConcluida(false);

        todoDtoMock = new TarefaDTO();
        todoDtoMock.setTitulo("Teste");
        todoDtoMock.setDescricao("Descrição de teste");
        todoDtoMock.setConcluida(false);
    }

    @Test
    void deveCriarTodoComSucesso() {
        // Given
        when(tarefaRepository.save(any(Todo.class))).thenReturn(todoMock);

        // When
        Todo resultado = tarefaService.criar(todoDtoMock);

        // Then
        assertNotNull(resultado);
        assertEquals("Teste", resultado.getTitulo());
        verify(tarefaRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void deveListarTodasAsTarefas() {
        // Given
        List<Tarefa> todosMock = Arrays.asList(todoMock);
        when(tarefaRepository.findAllByOrderByCriadoEmDesc()).thenReturn(todosMock);

        // When
        List<Tarefa> resultado = tarefaService.listarTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(tarefaRepository, times(1)).findAllByOrderByCriadoEmDesc();
    }

    @Test
    void deveBuscarTodoPorId() {
        // Given
        when(tarefaRepository.buscarPorId(1L)).thenReturn(Optional.of(todoMock));

        // When
        Todo resultado = tarefaService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(tarefaRepository, times(1)).buscarPorId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoTodoNaoEncontrado() {
        // Given
        when(tarefaRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            tarefaService.buscarPorId(999L);
        });
        verify(tarefaRepository, times(1)).buscarPorId(999L);
    }

    @Test
    void deveAlternarStatusDeConclusao() {
        // Given
        when(tarefaRepository.buscarPorId(1L)).thenReturn(Optional.of(todoMock));
        when(tarefaRepository.save(any(Todo.class))).thenReturn(todoMock);

        // When
        Todo resultado = tarefaService.toggleConcluida(1L);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.getConcluida()); // Status foi alternado
        verify(tarefaRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void deveAtualizarTodoComSucesso() {
        // Given
        when(tarefaRepository.buscarPorId(1L)).thenReturn(Optional.of(todoMock));
        when(tarefaRepository.save(any(Todo.class))).thenReturn(todoMock);

        TarefaDTO updateDto = new TarefaDTO();
        updateDto.setTitulo("Título Atualizado");
        updateDto.setDescricao("Descrição Atualizada");
        updateDto.setConcluida(true);

        // When
        Todo resultado = tarefaService.atualizar(1L, updateDto);

        // Then
        assertNotNull(resultado);
        verify(tarefaRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void deveDeletarTodoComSucesso() {
        // Given
        when(tarefaRepository.buscarPorId(1L)).thenReturn(Optional.of(todoMock));
        doNothing().when(tarefaRepository).delete(any(Todo.class));

        // When
        tarefaService.deletar(1L);

        // Then
        verify(tarefaRepository, times(1)).delete(any(Todo.class));
    }

    @Test
    void deveListarTodosPorStatus() {
        // Given
        List<Tarefa> todosMock = Arrays.asList(todoMock);
        when(tarefaRepository.findByConcluida(false)).thenReturn(todosMock);

        // When
        List<Tarefa> resultado = tarefaService.listarPorStatus(false);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(tarefaRepository, times(1)).findByConcluida(false);
    }
}
