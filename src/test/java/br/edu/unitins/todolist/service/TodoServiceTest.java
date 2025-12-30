package br.edu.unitins.todolist.service;

import br.edu.unitins.todolist.dto.TodoDTO;
import br.edu.unitins.todolist.exception.ResourceNotFoundException;
import br.edu.unitins.todolist.model.Todo;
import br.edu.unitins.todolist.repository.TodoRepository;
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
 * Testes unitários para TodoService
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
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo todoMock;
    private TodoDTO todoDtoMock;

    @BeforeEach
    void setUp() {
        todoMock = new Todo();
        todoMock.setId(1L);
        todoMock.setTitulo("Teste");
        todoMock.setDescricao("Descrição de teste");
        todoMock.setConcluida(false);

        todoDtoMock = new TodoDTO();
        todoDtoMock.setTitulo("Teste");
        todoDtoMock.setDescricao("Descrição de teste");
        todoDtoMock.setConcluida(false);
    }

    @Test
    void deveCriarTodoComSucesso() {
        // Given
        when(todoRepository.save(any(Todo.class))).thenReturn(todoMock);

        // When
        Todo resultado = todoService.criar(todoDtoMock);

        // Then
        assertNotNull(resultado);
        assertEquals("Teste", resultado.getTitulo());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void deveListarTodasAsTarefas() {
        // Given
        List<Todo> todosMock = Arrays.asList(todoMock);
        when(todoRepository.findAllByOrderByCriadoEmDesc()).thenReturn(todosMock);

        // When
        List<Todo> resultado = todoService.listarTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(todoRepository, times(1)).findAllByOrderByCriadoEmDesc();
    }

    @Test
    void deveBuscarTodoPorId() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoMock));

        // When
        Todo resultado = todoService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoTodoNaoEncontrado() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            todoService.buscarPorId(999L);
        });
        verify(todoRepository, times(1)).findById(999L);
    }

    @Test
    void deveAlternarStatusDeConclusao() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoMock));
        when(todoRepository.save(any(Todo.class))).thenReturn(todoMock);

        // When
        Todo resultado = todoService.toggleConcluida(1L);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.getConcluida()); // Status foi alternado
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void deveAtualizarTodoComSucesso() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoMock));
        when(todoRepository.save(any(Todo.class))).thenReturn(todoMock);

        TodoDTO updateDto = new TodoDTO();
        updateDto.setTitulo("Título Atualizado");
        updateDto.setDescricao("Descrição Atualizada");
        updateDto.setConcluida(true);

        // When
        Todo resultado = todoService.atualizar(1L, updateDto);

        // Then
        assertNotNull(resultado);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void deveDeletarTodoComSucesso() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoMock));
        doNothing().when(todoRepository).delete(any(Todo.class));

        // When
        todoService.deletar(1L);

        // Then
        verify(todoRepository, times(1)).delete(any(Todo.class));
    }

    @Test
    void deveListarTodosPorStatus() {
        // Given
        List<Todo> todosMock = Arrays.asList(todoMock);
        when(todoRepository.findByConcluida(false)).thenReturn(todosMock);

        // When
        List<Todo> resultado = todoService.listarPorStatus(false);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(todoRepository, times(1)).findByConcluida(false);
    }
}
