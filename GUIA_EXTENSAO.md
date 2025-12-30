# 🚀 Guia de Extensão do Projeto

Este documento fornece orientações sobre como estender o projeto Todo List com novas funcionalidades.

---

## 📋 Índice

1. [Adicionando Novos Campos](#1-adicionando-novos-campos)
2. [Criando Novos Endpoints](#2-criando-novos-endpoints)
3. [Implementando Validações Customizadas](#3-implementando-validações-customizadas)
4. [Adicionando Autenticação](#4-adicionando-autenticação)
5. [Criando Relacionamentos](#5-criando-relacionamentos)
6. [Configurando Testes](#6-configurando-testes)

---

## 1. Adicionando Novos Campos

### Exemplo: Adicionar campo de prioridade

#### Passo 1: Criar Enum de Prioridade

```java
package br.edu.unitins.todolist.model;

public enum Prioridade {
    ALTA("Alta"),
    MEDIA("Média"),
    BAIXA("Baixa");

    private final String descricao;

    Prioridade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
```

#### Passo 2: Adicionar campo na entidade Todo

```java
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Prioridade prioridade = Prioridade.MEDIA;

// Getter e Setter (Lombok já gera automaticamente)
```

#### Passo 3: Adicionar no DTO

```java
private Prioridade prioridade = Prioridade.MEDIA;
```

#### Passo 4: Atualizar Service

```java
public Todo criar(TodoDTO todoDTO) {
    Todo todo = new Todo();
    todo.setTitulo(todoDTO.getTitulo());
    todo.setDescricao(todoDTO.getDescricao());
    todo.setConcluida(todoDTO.getConcluida());
    todo.setPrioridade(todoDTO.getPrioridade()); // Nova linha
    return todoRepository.save(todo);
}
```

#### Passo 5: Atualizar Frontend

```html
<!-- index.html -->
<div class="form-group">
    <label for="prioridade">Prioridade</label>
    <select id="prioridade" name="prioridade">
        <option value="ALTA">Alta</option>
        <option value="MEDIA">Média</option>
        <option value="BAIXA">Baixa</option>
    </select>
</div>
```

```javascript
// app.js
const todoData = {
    titulo,
    descricao,
    concluida: false,
    prioridade: document.getElementById('prioridade').value
};
```

---

## 2. Criando Novos Endpoints

### Exemplo: Endpoint para estatísticas

#### Passo 1: Criar DTO de Resposta

```java
package br.edu.unitins.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstatisticasDTO {
    private Long total;
    private Long concluidas;
    private Long pendentes;
    private Double percentualConclusao;
}
```

#### Passo 2: Adicionar método no Service

```java
@Transactional(readOnly = true)
public EstatisticasDTO obterEstatisticas() {
    List<Todo> todos = todoRepository.findAll();
    long total = todos.size();
    long concluidas = todos.stream().filter(Todo::getConcluida).count();
    long pendentes = total - concluidas;
    double percentual = total > 0 ? (concluidas * 100.0 / total) : 0;

    return new EstatisticasDTO(total, concluidas, pendentes, percentual);
}
```

#### Passo 3: Adicionar endpoint no Controller

```java
@GetMapping("/estatisticas")
public ResponseEntity<EstatisticasDTO> obterEstatisticas() {
    log.info("Requisição GET /v1/todos/estatisticas");
    EstatisticasDTO stats = todoService.obterEstatisticas();
    return ResponseEntity.ok(stats);
}
```

---

## 3. Implementando Validações Customizadas

### Exemplo: Validar data de vencimento não pode ser no passado

#### Passo 1: Criar anotação de validação

```java
package br.edu.unitins.todolist.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FuturaOuHojeValidator.class)
public @interface FuturaOuHoje {
    String message() default "A data deve ser hoje ou no futuro";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

#### Passo 2: Implementar o validador

```java
package br.edu.unitins.todolist.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FuturaOuHojeValidator
    implements ConstraintValidator<FuturaOuHoje, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null será tratado por @NotNull se necessário
        }
        return !value.isBefore(LocalDate.now());
    }
}
```

#### Passo 3: Usar no DTO

```java
@FuturaOuHoje
private LocalDate dataVencimento;
```

---

## 4. Adicionando Autenticação

### Passo 1: Adicionar dependências

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
```

### Passo 2: Criar entidade Usuario

```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String senha; // Armazenar hash

    private String nome;

    @OneToMany(mappedBy = "usuario")
    private List<Todo> todos;
}
```

### Passo 3: Configurar Spring Security

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 5. Criando Relacionamentos

### Exemplo: Categorias para Todos

#### Passo 1: Criar entidade Categoria

```java
@Entity
@Table(name = "categorias")
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cor;

    @OneToMany(mappedBy = "categoria")
    private List<Todo> todos;
}
```

#### Passo 2: Adicionar relacionamento em Todo

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "categoria_id")
private Categoria categoria;
```

#### Passo 3: Criar Repository e Service para Categoria

```java
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNome(String nome);
}
```

---

## 6. Configurando Testes

### Teste de Integração com TestContainers

#### Passo 1: Adicionar dependência

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <scope>test</scope>
</dependency>
```

#### Passo 2: Criar teste de integração

```java
@SpringBootTest
@Testcontainers
class TodoIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void deveSalvarERecuperarTodo() {
        Todo todo = new Todo();
        todo.setTitulo("Teste Integração");
        todo.setConcluida(false);

        Todo saved = todoRepository.save(todo);

        assertNotNull(saved.getId());
        assertEquals("Teste Integração", saved.getTitulo());
    }
}
```

---

## 📚 Recursos Adicionais

### Documentação Útil

- [Spring Data JPA - Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)
- [Bean Validation](https://beanvalidation.org/2.0/spec/)
- [Spring Security](https://spring.io/projects/spring-security)
- [TestContainers](https://www.testcontainers.org/)

### Próximos Passos Sugeridos

1. Implementar paginação nas listagens
2. Adicionar cache com Redis
3. Implementar upload de arquivos anexos
4. Criar notificações por email
5. Adicionar busca full-text com Elasticsearch
6. Implementar versionamento de API (v2)
7. Adicionar rate limiting
8. Implementar soft delete

---

**Boa sorte com suas extensões! 🚀**

*Prof. Alysson - UNITINS TOGraduado*
