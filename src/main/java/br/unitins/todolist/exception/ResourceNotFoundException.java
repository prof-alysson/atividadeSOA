package br.unitins.todolist.exception;

/**
 * Exceção lançada quando uma tarefa não é encontrada no banco de dados
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
