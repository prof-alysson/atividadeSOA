package br.edu.unitins.todolist.repository;

import br.edu.unitins.todolist.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para operações de banco de dados da entidade Todo
 *
 * Boas práticas:
 * - Uso de Spring Data JPA para abstração de acesso a dados
 * - Métodos de consulta derivados do nome (Query Methods)
 * - Separação da lógica de persistência
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * Busca todas as tarefas concluídas ou não concluídas
     * Query Method - Spring Data JPA cria a query automaticamente
     */
    List<Todo> findByConcluida(Boolean concluida);

    /**
     * Busca tarefas por título contendo o texto (case insensitive)
     */
    List<Todo> findByTituloContainingIgnoreCase(String titulo);

    /**
     * Busca todas as tarefas ordenadas por data de criação decrescente
     */
    List<Todo> findAllByOrderByCriadoEmDesc();
}
