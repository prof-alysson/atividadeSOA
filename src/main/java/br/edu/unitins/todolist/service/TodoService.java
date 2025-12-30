package br.edu.unitins.todolist.service;

import br.edu.unitins.todolist.dto.TodoDTO;
import br.edu.unitins.todolist.exception.ResourceNotFoundException;
import br.edu.unitins.todolist.model.Todo;
import br.edu.unitins.todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço para lógica de negócio de Todo
 *
 * Boas práticas aplicadas:
 * - Separação de responsabilidades (camada de serviço)
 * - Transações gerenciadas pelo Spring
 * - Logging com SLF4J
 * - Injeção de dependência por construtor (imutável)
 * - Validações de negócio
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    /**
     * Lista todas as tarefas ordenadas por data de criação
     */
    @Transactional(readOnly = true)
    public List<Todo> listarTodas() {
        log.info("Listando todas as tarefas");
        return todoRepository.findAllByOrderByCriadoEmDesc();
    }

    /**
     * Busca uma tarefa por ID
     */
    @Transactional(readOnly = true)
    public Todo buscarPorId(Long id) {
        log.info("Buscando tarefa com ID: {}", id);
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));
    }

    /**
     * Cria uma nova tarefa
     */
    public Todo criar(TodoDTO todoDTO) {
        log.info("Criando nova tarefa: {}", todoDTO.getTitulo());

        Todo todo = new Todo();
        todo.setTitulo(todoDTO.getTitulo());
        todo.setDescricao(todoDTO.getDescricao());
        todo.setConcluida(todoDTO.getConcluida() != null ? todoDTO.getConcluida() : false);

        Todo savedTodo = todoRepository.save(todo);
        log.info("Tarefa criada com sucesso. ID: {}", savedTodo.getId());

        return savedTodo;
    }

    /**
     * Atualiza uma tarefa existente
     */
    public Todo atualizar(Long id, TodoDTO todoDTO) {
        log.info("Atualizando tarefa ID: {}", id);

        Todo todo = buscarPorId(id);
        todo.setTitulo(todoDTO.getTitulo());
        todo.setDescricao(todoDTO.getDescricao());
        todo.setConcluida(todoDTO.getConcluida() != null ? todoDTO.getConcluida() : false);

        Todo updatedTodo = todoRepository.save(todo);
        log.info("Tarefa atualizada com sucesso. ID: {}", updatedTodo.getId());

        return updatedTodo;
    }

    /**
     * Alterna o status de conclusão de uma tarefa
     */
    public Todo toggleConcluida(Long id) {
        log.info("Alternando status de conclusão da tarefa ID: {}", id);

        Todo todo = buscarPorId(id);
        todo.setConcluida(!todo.getConcluida());

        Todo updatedTodo = todoRepository.save(todo);
        log.info("Status alterado. Tarefa ID: {} - Concluída: {}", id, updatedTodo.getConcluida());

        return updatedTodo;
    }

    /**
     * Deleta uma tarefa
     */
    public void deletar(Long id) {
        log.info("Deletando tarefa ID: {}", id);

        Todo todo = buscarPorId(id);
        todoRepository.delete(todo);

        log.info("Tarefa deletada com sucesso. ID: {}", id);
    }

    /**
     * Lista tarefas por status de conclusão
     */
    @Transactional(readOnly = true)
    public List<Todo> listarPorStatus(Boolean concluida) {
        log.info("Listando tarefas com status concluída: {}", concluida);
        return todoRepository.findByConcluida(concluida);
    }

    /**
     * Busca tarefas por título
     */
    @Transactional(readOnly = true)
    public List<Todo> buscarPorTitulo(String titulo) {
        log.info("Buscando tarefas com título contendo: {}", titulo);
        return todoRepository.findByTituloContainingIgnoreCase(titulo);
    }
}
