package br.edu.unitins.todolist.controller;

import br.edu.unitins.todolist.dto.TodoDTO;
import br.edu.unitins.todolist.model.Todo;
import br.edu.unitins.todolist.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Todos
 *
 * Boas práticas aplicadas:
 * - API RESTful com verbos HTTP apropriados
 * - Versionamento de API (/v1)
 * - CORS habilitado para frontend
 * - Validação de entrada
 * - Respostas com status HTTP adequados
 * - Documentação inline
 *
 * Endpoints disponíveis:
 * - GET    /v1/todos         - Lista todas as tarefas
 * - GET    /v1/todos/{id}    - Busca tarefa por ID
 * - POST   /v1/todos         - Cria nova tarefa
 * - PUT    /v1/todos/{id}    - Atualiza tarefa
 * - PATCH  /v1/todos/{id}/toggle - Alterna status de conclusão
 * - DELETE /v1/todos/{id}    - Deleta tarefa
 * - GET    /v1/todos/status/{concluida} - Lista por status
 * - GET    /v1/todos/search?titulo=xxx - Busca por título
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@RestController
@RequestMapping("/v1/todos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Em produção, especificar origem exata
public class TodoController {

    private final TodoService todoService;

    /**
     * Lista todas as tarefas
     * GET /api/v1/todos
     */
    @GetMapping
    public ResponseEntity<List<Todo>> listarTodas() {
        log.info("Requisição GET /v1/todos");
        List<Todo> todos = todoService.listarTodas();
        return ResponseEntity.ok(todos);
    }

    /**
     * Busca tarefa por ID
     * GET /api/v1/todos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Todo> buscarPorId(@PathVariable Long id) {
        log.info("Requisição GET /v1/todos/{}", id);
        Todo todo = todoService.buscarPorId(id);
        return ResponseEntity.ok(todo);
    }

    /**
     * Cria uma nova tarefa
     * POST /api/v1/todos
     */
    @PostMapping
    public ResponseEntity<Todo> criar(@Valid @RequestBody TodoDTO todoDTO) {
        log.info("Requisição POST /v1/todos - {}", todoDTO);
        Todo todo = todoService.criar(todoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }

    /**
     * Atualiza uma tarefa existente
     * PUT /api/v1/todos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Todo> atualizar(@PathVariable Long id, @Valid @RequestBody TodoDTO todoDTO) {
        log.info("Requisição PUT /v1/todos/{} - {}", id, todoDTO);
        Todo todo = todoService.atualizar(id, todoDTO);
        return ResponseEntity.ok(todo);
    }

    /**
     * Alterna o status de conclusão da tarefa
     * PATCH /api/v1/todos/{id}/toggle
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleConcluida(@PathVariable Long id) {
        log.info("Requisição PATCH /v1/todos/{}/toggle", id);
        Todo todo = todoService.toggleConcluida(id);
        return ResponseEntity.ok(todo);
    }

    /**
     * Deleta uma tarefa
     * DELETE /api/v1/todos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Requisição DELETE /v1/todos/{}", id);
        todoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista tarefas por status de conclusão
     * GET /api/v1/todos/status/{concluida}
     */
    @GetMapping("/status/{concluida}")
    public ResponseEntity<List<Todo>> listarPorStatus(@PathVariable Boolean concluida) {
        log.info("Requisição GET /v1/todos/status/{}", concluida);
        List<Todo> todos = todoService.listarPorStatus(concluida);
        return ResponseEntity.ok(todos);
    }

    /**
     * Busca tarefas por título
     * GET /api/v1/todos/search?titulo=xxx
     */
    @GetMapping("/search")
    public ResponseEntity<List<Todo>> buscarPorTitulo(@RequestParam String titulo) {
        log.info("Requisição GET /v1/todos/search?titulo={}", titulo);
        List<Todo> todos = todoService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(todos);
    }
}
