package br.unitins.todolist.repository;

import br.unitins.todolist.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Responsável por acessar o banco de dados e fazer operações com tarefas
 * O Spring cria automaticamente a implementação deste código
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Busca tarefas concluídas ou pendentes
    List<Tarefa> findByConcluida(Boolean concluida);

    // Busca tarefas que contém um texto no título (ignora maiúsculas/minúsculas)
    List<Tarefa> findByTituloContainingIgnoreCase(String titulo);

    // Busca todas as tarefas ordenadas pela mais recente
    List<Tarefa> findAllByOrderByCriadoEmDesc();
}
