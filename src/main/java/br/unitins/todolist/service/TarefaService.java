package br.unitins.todolist.service;

import br.unitins.todolist.dto.TarefaDTO;
import br.unitins.todolist.exception.ResourceNotFoundException;
import br.unitins.todolist.model.Tarefa;
import br.unitins.todolist.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contém a lógica de negócio para gerenciar tarefas
 * Esta classe fica entre o Controller (que recebe requisições) e o Repository (que acessa o banco)
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    @Transactional(readOnly = true)
    public List<Tarefa> listarTodas() {
        log.info("Listando todas as tarefas");
        return tarefaRepository.findAllByOrderByCriadoEmDesc();
    }

    @Transactional(readOnly = true)
    public Tarefa buscarPorId(Long id) {
        log.info("Buscando tarefa com ID: {}", id);
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));
    }

    public Tarefa criar(TarefaDTO tarefaDTO) {
        log.info("Criando nova tarefa: {}", tarefaDTO.getTitulo());

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setConcluida(tarefaDTO.getConcluida() != null ? tarefaDTO.getConcluida() : false);

        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizar(Long id, TarefaDTO tarefaDTO) {
        log.info("Atualizando tarefa ID: {}", id);

        Tarefa tarefa = buscarPorId(id);
        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setConcluida(tarefaDTO.getConcluida() != null ? tarefaDTO.getConcluida() : false);

        return tarefaRepository.save(tarefa);
    }

    public Tarefa toggleConcluida(Long id) {
        log.info("Alternando status da tarefa ID: {}", id);

        Tarefa tarefa = buscarPorId(id);
        tarefa.setConcluida(!tarefa.getConcluida());

        return tarefaRepository.save(tarefa);
    }

    public void deletar(Long id) {
        log.info("Deletando tarefa ID: {}", id);
        Tarefa tarefa = buscarPorId(id);
        tarefaRepository.delete(tarefa);
    }

    @Transactional(readOnly = true)
    public List<Tarefa> listarPorStatus(Boolean concluida) {
        log.info("Listando tarefas - concluída: {}", concluida);
        return tarefaRepository.findByConcluida(concluida);
    }

    @Transactional(readOnly = true)
    public List<Tarefa> buscarPorTitulo(String titulo) {
        log.info("Buscando tarefas com título: {}", titulo);
        return tarefaRepository.findByTituloContainingIgnoreCase(titulo);
    }
}
