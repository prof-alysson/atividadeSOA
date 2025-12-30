package br.edu.unitins.todolist.exception;

/**
 * Exceção customizada para recursos não encontrados
 *
 * Boas práticas:
 * - Exceções específicas do domínio
 * - Tratamento de erros centralizado
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
