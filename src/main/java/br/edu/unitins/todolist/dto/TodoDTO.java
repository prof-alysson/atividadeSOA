package br.edu.unitins.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para transferência de dados de Todo
 *
 * Boas práticas:
 * - Separação entre camada de apresentação e modelo de domínio
 * - Validações de entrada
 * - Imutabilidade de dados entre camadas
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String titulo;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    private Boolean concluida = false;
}
