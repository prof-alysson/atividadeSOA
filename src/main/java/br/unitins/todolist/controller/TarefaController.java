package br.unitins.todolist.controller;

import br.unitins.todolist.dto.TarefaDTO;
import br.unitins.todolist.model.Tarefa;
import br.unitins.todolist.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Recebe as requisições HTTP e chama os métodos do Service
 * Este é o ponto de entrada da API REST
 *
 * Endpoints disponíveis:
 * - GET    /v1/tarefas                      - Lista todas as tarefas
 * - GET    /v1/tarefas/{id}                 - Busca tarefa por ID
 * - POST   /v1/tarefas                      - Cria nova tarefa
 * - PUT    /v1/tarefas/{id}                 - Atualiza tarefa
 * - PATCH  /v1/tarefas/{id}/toggle          - Marca/desmarca como concluída
 * - DELETE /v1/tarefas/{id}                 - Deleta tarefa
 * - GET    /v1/tarefas/status/{concluida}   - Lista por status
 * - GET    /v1/tarefas/search?titulo=xxx    - Busca por título
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@RestController
@RequestMapping("/v1/tarefas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Permite requisições de qualquer origem (apenas para didática)
public class TarefaController {

    private final TarefaService tarefaService;

    // GET /api/v1/tarefas - Lista todas as tarefas
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        log.info("GET /v1/tarefas");
        return ResponseEntity.ok(tarefaService.listarTodas());
    }

    // GET /api/v1/tarefas/{id} - Busca uma tarefa pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        log.info("GET /v1/tarefas/{}", id);
        return ResponseEntity.ok(tarefaService.buscarPorId(id));
    }

    // POST /api/v1/tarefas - Cria uma nova tarefa
    @PostMapping
    public ResponseEntity<Tarefa> criar(@Valid @RequestBody TarefaDTO tarefaDTO) {
        log.info("POST /v1/tarefas");
        Tarefa tarefa = tarefaService.criar(tarefaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefa);
    }

    // PUT /api/v1/tarefas/{id} - Atualiza uma tarefa existente
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaDTO tarefaDTO) {
        log.info("PUT /v1/tarefas/{}", id);
        return ResponseEntity.ok(tarefaService.atualizar(id, tarefaDTO));
    }

    // PATCH /api/v1/tarefas/{id}/toggle - Marca/desmarca como concluída
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Tarefa> toggleConcluida(@PathVariable Long id) {
        log.info("PATCH /v1/tarefas/{}/toggle", id);
        return ResponseEntity.ok(tarefaService.toggleConcluida(id));
    }

    // DELETE /api/v1/tarefas/{id} - Deleta uma tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /v1/tarefas/{}", id);
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/v1/tarefas/status/{concluida} - Lista tarefas por status
    @GetMapping("/status/{concluida}")
    public ResponseEntity<List<Tarefa>> listarPorStatus(@PathVariable Boolean concluida) {
        log.info("GET /v1/tarefas/status/{}", concluida);
        return ResponseEntity.ok(tarefaService.listarPorStatus(concluida));
    }

    // GET /api/v1/tarefas/search?titulo=xxx - Busca tarefas por título
    @GetMapping("/search")
    public ResponseEntity<List<Tarefa>> buscarPorTitulo(@RequestParam String titulo) {
        log.info("GET /v1/tarefas/search?titulo={}", titulo);
        return ResponseEntity.ok(tarefaService.buscarPorTitulo(titulo));
    }
}
