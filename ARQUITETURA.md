# 🏗️ Documentação de Arquitetura

## Todo List Microservice - UNITINS TOGraduado

Este documento descreve a arquitetura técnica do projeto para fins educacionais.

---

## 📐 Visão Geral da Arquitetura

### Diagrama de Alto Nível

```
┌─────────────────────────────────────────────────────────┐
│                    USUÁRIO                              │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              CAMADA DE APRESENTAÇÃO                     │
│  ┌─────────────────────────────────────────────────┐   │
│  │   Frontend (HTML/CSS/JavaScript)                │   │
│  │   - Nginx Container (Porta 80)                  │   │
│  │   - SPA (Single Page Application)               │   │
│  └─────────────────────────────────────────────────┘   │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP/REST
                     ▼
┌─────────────────────────────────────────────────────────┐
│              CAMADA DE APLICAÇÃO                        │
│  ┌─────────────────────────────────────────────────┐   │
│  │   Spring Boot Application (Porta 8080)          │   │
│  │                                                  │   │
│  │   ┌──────────────────────────────────────┐      │   │
│  │   │     Controller Layer (REST API)      │      │   │
│  │   └──────────────┬───────────────────────┘      │   │
│  │                  │                               │   │
│  │   ┌──────────────▼───────────────────────┐      │   │
│  │   │     Service Layer (Business Logic)   │      │   │
│  │   └──────────────┬───────────────────────┘      │   │
│  │                  │                               │   │
│  │   ┌──────────────▼───────────────────────┐      │   │
│  │   │   Repository Layer (Data Access)     │      │   │
│  │   └──────────────┬───────────────────────┘      │   │
│  │                                                  │   │
│  └──────────────────┼──────────────────────────────┘   │
└───────────────────┬─┴──────────────────────────────────┘
                    │ JDBC
                    ▼
┌─────────────────────────────────────────────────────────┐
│              CAMADA DE PERSISTÊNCIA                     │
│  ┌─────────────────────────────────────────────────┐   │
│  │   MySQL Database (Porta 3306)                   │   │
│  │   - Banco: tododb                               │   │
│  │   - Tabela: todos                               │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

---

## 🔧 Componentes Principais

### 1. Frontend (Nginx + HTML/JS)

**Responsabilidades:**
- Renderização da interface do usuário
- Captura de eventos e interações
- Comunicação com a API REST
- Validação básica de formulários

**Tecnologias:**
- HTML5
- CSS3
- JavaScript Vanilla (ES6+)
- Nginx Alpine (servidor web)

**Arquivos:**
- `index.html` - Estrutura da página
- `style.css` - Estilos e layout
- `app.js` - Lógica de negócio do frontend

### 2. Backend (Spring Boot)

**Responsabilidades:**
- Exposição de API REST
- Validação de dados
- Lógica de negócio
- Persistência de dados
- Tratamento de exceções
- Logging

**Tecnologias:**
- Java 17
- Spring Boot 3.2.1
- Spring Web (REST)
- Spring Data JPA
- Hibernate
- Bean Validation
- Lombok
- Spring Actuator

**Estrutura de Pacotes:**

```
br.edu.unitins.todolist
├── TodoListApplication.java         # Classe principal
├── controller/                       # Camada de apresentação
│   └── TodoController.java
├── service/                          # Camada de negócio
│   └── TodoService.java
├── repository/                       # Camada de dados
│   └── TodoRepository.java
├── model/                            # Entidades do domínio
│   └── Todo.java
├── dto/                              # Objetos de transferência
│   └── TodoDTO.java
└── exception/                        # Tratamento de erros
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

### 3. Banco de Dados (MySQL)

**Responsabilidades:**
- Armazenamento persistente
- Integridade referencial
- Transações ACID

**Esquema:**

```sql
CREATE TABLE todos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    concluida BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME,
    INDEX idx_concluida (concluida),
    INDEX idx_criado_em (criado_em)
);
```

---

## 🔄 Fluxo de Dados

### Criação de uma Tarefa (POST)

```
1. Usuário preenche formulário no Frontend
   ↓
2. JavaScript valida dados localmente
   ↓
3. Envia POST para /api/v1/todos
   ↓
4. TodoController recebe requisição
   ↓
5. @Valid valida TodoDTO
   ↓
6. TodoController chama TodoService.criar()
   ↓
7. TodoService converte DTO → Entity
   ↓
8. TodoService chama TodoRepository.save()
   ↓
9. JPA/Hibernate gera SQL INSERT
   ↓
10. MySQL persiste dados
   ↓
11. Retorna Todo criado (com ID)
   ↓
12. Controller retorna 201 Created
   ↓
13. Frontend atualiza a lista
```

### Listagem de Tarefas (GET)

```
1. Usuário acessa a página ou clica em filtro
   ↓
2. JavaScript faz GET para /api/v1/todos
   ↓
3. TodoController.listarTodas()
   ↓
4. TodoService.listarTodas()
   ↓
5. TodoRepository.findAllByOrderByCriadoEmDesc()
   ↓
6. JPA executa SELECT * FROM todos ORDER BY criado_em DESC
   ↓
7. Hibernate mapeia ResultSet → List<Todo>
   ↓
8. Controller retorna 200 OK + JSON
   ↓
9. Frontend renderiza lista
```

---

## 🎯 Padrões de Projeto Utilizados

### 1. **Layered Architecture (Arquitetura em Camadas)**

Separação clara de responsabilidades:
- **Presentation:** Controllers
- **Business:** Services
- **Persistence:** Repositories
- **Domain:** Models

### 2. **Repository Pattern**

Abstração do acesso a dados:
```java
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByConcluida(Boolean concluida);
}
```

### 3. **Data Transfer Object (DTO)**

Separação entre camadas:
```java
TodoDTO (API) → Todo (Domínio) → Tabela (DB)
```

### 4. **Dependency Injection (DI)**

Inversão de controle via Spring:
```java
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository; // Injetado
}
```

### 5. **RESTful API**

Recursos e verbos HTTP:
- GET /todos - Lista
- POST /todos - Cria
- PUT /todos/{id} - Atualiza
- DELETE /todos/{id} - Deleta

### 6. **Exception Handler**

Tratamento centralizado de erros:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    // ...
}
```

---

## 🔐 Segurança

### Práticas Implementadas

1. **Validação de Entrada**
   - Bean Validation (@NotBlank, @Size)
   - Sanitização no frontend

2. **Container Não-Root**
   ```dockerfile
   RUN adduser -S spring
   USER spring:spring
   ```

3. **Escape de HTML**
   ```javascript
   escapeHtml(text) // Previne XSS
   ```

4. **CORS Configurado**
   ```java
   @CrossOrigin(origins = "*") // Em produção: especificar domínio
   ```

### Melhorias Recomendadas para Produção

- [ ] Implementar autenticação (JWT)
- [ ] HTTPS obrigatório
- [ ] Rate limiting
- [ ] Input sanitization no backend
- [ ] CORS restritivo
- [ ] SQL Injection protection (já feito pelo JPA)

---

## 📊 Monitoramento e Observabilidade

### Spring Actuator Endpoints

```
GET /api/actuator/health       # Status da aplicação
GET /api/actuator/info         # Informações
GET /api/actuator/metrics      # Métricas
```

### Health Checks Docker

```yaml
healthcheck:
  test: ["CMD", "wget", "--spider", "http://localhost:8080/api/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 3
```

### Logging

```java
@Slf4j
public class TodoService {
    log.info("Criando nova tarefa: {}", titulo);
    log.error("Erro ao buscar tarefa: {}", id);
}
```

---

## 🚀 DevOps e Deploy

### Containerização

**Multi-stage Build:**
```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
# ... build da aplicação

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
# ... apenas JRE + JAR
```

**Benefícios:**
- Imagem final menor (~200MB vs ~600MB)
- Mais segura
- Deploy mais rápido

### Orquestração

**Docker Compose:**
```yaml
services:
  mysql:     # Banco de dados
  app:       # Backend Spring Boot
  frontend:  # Nginx
```

**Networking:**
- Bridge network isolada
- Comunicação entre containers por nome

**Volumes:**
- Persistência de dados MySQL
- Sobrevive a restarts

### CI/CD (Sugestão)

```yaml
# Exemplo de pipeline GitHub Actions
- Build da aplicação
- Testes unitários
- Testes de integração
- Build da imagem Docker
- Push para registry
- Deploy
```

---

## 📈 Escalabilidade

### Atual (Single Instance)

```
[Client] → [Nginx] → [Spring Boot] → [MySQL]
```

### Futuro (Escalável)

```
                    ┌─→ [Spring Boot Instance 1] ─┐
[Client] → [Load Balancer] ─→ [Spring Boot Instance 2] ─→ [MySQL Master]
                    └─→ [Spring Boot Instance 3] ─┘            ↓
                                                         [MySQL Replica]
```

### Considerações:

1. **Stateless:** Aplicação já é stateless (sem sessão)
2. **Cache:** Adicionar Redis para performance
3. **Database:** Replicação MySQL (read replicas)
4. **Load Balancer:** Nginx ou HAProxy
5. **Mensageria:** RabbitMQ/Kafka para eventos

---

## 🧪 Testabilidade

### Níveis de Teste

```
┌─────────────────────────────────┐
│   End-to-End Tests (E2E)        │  ← Testa fluxo completo
├─────────────────────────────────┤
│   Integration Tests             │  ← Testa componentes integrados
├─────────────────────────────────┤
│   Unit Tests                    │  ← Testa unidades isoladas
└─────────────────────────────────┘
```

### Estratégia Implementada

**Testes Unitários:**
```java
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoService service;

    @Test
    void deveCriarTodoComSucesso() { }
}
```

---

## 📚 Referências Técnicas

### Design Patterns
- [Martin Fowler - Patterns of Enterprise Application Architecture](https://martinfowler.com/eaaCatalog/)

### REST
- [Roy Fielding - REST Dissertation](https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm)

### Spring
- [Spring Framework Documentation](https://spring.io/projects/spring-framework)
- [Spring Boot Best Practices](https://spring.io/guides)

### Docker
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Multi-stage Builds](https://docs.docker.com/build/building/multi-stage/)

---

**Desenvolvido para fins educacionais**

*Prof. Alysson - UNITINS TOGraduado*
