package br.unitins.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto usado para enviar e receber dados de tarefas pela API
 * (não inclui campos como id e datas, que são gerados automaticamente)
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String titulo;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    private Boolean concluida = false;
}
