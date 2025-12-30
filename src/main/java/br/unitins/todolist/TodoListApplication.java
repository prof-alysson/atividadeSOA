package br.unitins.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Todo List
 *
 * @author Prof. Alysson - UNITINS TOGraduado
 */
@SpringBootApplication
public class TodoListApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoListApplication.class, args);
        System.out.println("===========================================");
        System.out.println("Todo List Microservice - UNITINS");
        System.out.println("Aplicação iniciada com sucesso!");
        System.out.println("===========================================");
    }
}
