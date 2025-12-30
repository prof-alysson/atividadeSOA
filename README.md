# 📝 Todo List Microservice

Projeto didático de Computação Orientada a Serviços
**Professor:** Alysson
**Instituição:** UNITINS - Universidade Estadual do Tocantins
**Programa:** TOGraduado - Ensino Tecnológico EAD

---

## 📚 Sobre o Projeto

Este é um projeto modelo didático de uma aplicação **Todo List** (lista de tarefas) desenvolvida com arquitetura de microserviços, aplicando boas práticas de **DevOps** e desenvolvimento moderno.

### 🎯 Objetivos de Aprendizagem

- Compreender a arquitetura de microserviços
- Aplicar boas práticas de desenvolvimento backend com Spring Boot
- Implementar API RESTful
- Trabalhar com containerização usando Docker
- Integrar frontend e backend
- Utilizar banco de dados relacional (MySQL)
- Aplicar princípios de DevOps

---

## 🏗️ Arquitetura do Projeto

### Stack Tecnológica

**Backend:**
- ☕ Java 17
- 🍃 Spring Boot 3.2.1
- 🗄️ MySQL 8.0
- 🔧 Maven
- 📦 Docker

**Frontend:**
- 🌐 HTML5
- 🎨 CSS3
- ⚡ JavaScript (Vanilla)

### Arquitetura em Camadas

```
┌─────────────────────────────────────┐
│         Frontend (HTML/JS)          │
│    http://localhost:80              │
└──────────────┬──────────────────────┘
               │ HTTP/REST
┌──────────────▼──────────────────────┐
│      API REST (Controller)          │
│    http://localhost:8080/api        │
├─────────────────────────────────────┤
│         Service Layer               │
│    (Lógica de Negócio)             │
├─────────────────────────────────────┤
│      Repository Layer               │
│    (Acesso a Dados - JPA)          │
└──────────────┬──────────────────────┘
               │ JDBC
┌──────────────▼──────────────────────┐
│         MySQL Database              │
│    localhost:3306/tododb            │
└─────────────────────────────────────┘
```

---

## 📁 Estrutura do Projeto

```
SOA/
├── src/
│   └── main/
│       ├── java/br/edu/unitins/todolist/
│       │   ├── TodoListApplication.java      # Classe principal
│       │   ├── controller/
│       │   │   └── TodoController.java       # API REST
│       │   ├── service/
│       │   │   └── TodoService.java          # Lógica de negócio
│       │   ├── repository/
│       │   │   └── TodoRepository.java       # Acesso a dados
│       │   ├── model/
│       │   │   └── Todo.java                 # Entidade
│       │   ├── dto/
│       │   │   └── TodoDTO.java              # Data Transfer Object
│       │   └── exception/
│       │       ├── ResourceNotFoundException.java
│       │       └── GlobalExceptionHandler.java
│       └── resources/
│           ├── application.yml               # Configurações
│           └── static/                       # Frontend
│               ├── index.html
│               ├── style.css
│               └── app.js
├── Dockerfile                                 # Imagem Docker da aplicação
├── docker-compose.yml                         # Orquestração de containers
└── pom.xml                                    # Dependências Maven
```

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

1. **Docker** e **Docker Compose** instalados
   - [Instalar Docker](https://docs.docker.com/get-docker/)
   - [Instalar Docker Compose](https://docs.docker.com/compose/install/)

2. **Portas disponíveis:**
   - `3306` (MySQL)
   - `8080` (Backend API)
   - `80` (Frontend)

### Passo a Passo

#### 1️⃣ Clone ou baixe o projeto

```bash
cd SOA
```

#### 2️⃣ Execute com Docker Compose

```bash
docker-compose up --build
```

Este comando irá:
- Criar o banco de dados MySQL
- Compilar a aplicação Spring Boot
- Iniciar todos os serviços

#### 3️⃣ Aguarde a inicialização

Aguarde até ver a mensagem:
```
Todo List Microservice - UNITINS
Aplicação iniciada com sucesso!
```

#### 4️⃣ Acesse a aplicação

- **Frontend:** http://localhost
- **API REST:** http://localhost:8080/api/v1/todos
- **Health Check:** http://localhost:8080/api/actuator/health

### Parar a Aplicação

```bash
# Parar containers
docker-compose down

# Parar e remover volumes (limpa o banco de dados)
docker-compose down -v
```

---

## 🔌 API REST - Endpoints

### Base URL
```
http://localhost:8080/api/v1/todos
```

### Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/v1/todos` | Lista todas as tarefas |
| `GET` | `/v1/todos/{id}` | Busca tarefa por ID |
| `POST` | `/v1/todos` | Cria nova tarefa |
| `PUT` | `/v1/todos/{id}` | Atualiza tarefa |
| `PATCH` | `/v1/todos/{id}/toggle` | Alterna status |
| `DELETE` | `/v1/todos/{id}` | Deleta tarefa |
| `GET` | `/v1/todos/status/{concluida}` | Lista por status |
| `GET` | `/v1/todos/search?titulo=xxx` | Busca por título |

### Exemplos de Requisições

#### Criar Tarefa
```bash
curl -X POST http://localhost:8080/api/v1/todos \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Estudar Spring Boot",
    "descricao": "Revisar conceitos de microserviços",
    "concluida": false
  }'
```

#### Listar Todas
```bash
curl http://localhost:8080/api/v1/todos
```

#### Alternar Status
```bash
curl -X PATCH http://localhost:8080/api/v1/todos/1/toggle
```

#### Deletar Tarefa
```bash
curl -X DELETE http://localhost:8080/api/v1/todos/1
```

---

## 🎓 Conceitos e Boas Práticas Aplicadas

### 1. **Arquitetura em Camadas**

O projeto segue o padrão de arquitetura em camadas:

- **Controller:** Recebe requisições HTTP
- **Service:** Contém lógica de negócio
- **Repository:** Acessa o banco de dados
- **Model:** Representa as entidades do domínio

**Benefícios:**
- Separação de responsabilidades
- Facilita manutenção
- Permite testes unitários
- Código mais organizado

### 2. **API RESTful**

A API segue os princípios REST:

- ✅ Uso correto de verbos HTTP (GET, POST, PUT, PATCH, DELETE)
- ✅ Status codes apropriados (200, 201, 404, 400, 500)
- ✅ Recursos bem definidos (/todos)
- ✅ Stateless (sem estado no servidor)

### 3. **Microserviços**

Características de microserviço implementadas:

- 🔹 Serviço independente e auto-contido
- 🔹 Comunicação via API REST
- 🔹 Banco de dados próprio
- 🔹 Containerizado com Docker
- 🔹 Health checks para monitoramento

### 4. **DevOps**

Práticas de DevOps aplicadas:

- 🐳 **Containerização:** Docker e Docker Compose
- 🏥 **Health Checks:** Monitoramento de saúde
- 📊 **Observability:** Spring Actuator
- 🔧 **Configuração Externalizada:** Variáveis de ambiente
- 🏗️ **Multi-stage Build:** Otimização de imagem Docker

### 5. **Segurança**

- ✅ Usuário não-root no container
- ✅ Validação de entrada de dados
- ✅ Escape de HTML no frontend (proteção XSS)
- ✅ Tratamento de exceções centralizado

### 6. **Clean Code**

- 📝 Código bem documentado
- 🏷️ Nomes descritivos
- 🎯 Métodos com responsabilidade única
- 🔄 Uso de DTOs para transferência de dados
- 🚫 Evita código duplicado

---

## 🗄️ Banco de Dados

### Estrutura da Tabela `todos`

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | BIGINT | Primary Key, Auto Increment |
| `titulo` | VARCHAR(100) | Título da tarefa (obrigatório) |
| `descricao` | VARCHAR(500) | Descrição detalhada (opcional) |
| `concluida` | BOOLEAN | Status de conclusão |
| `criado_em` | DATETIME | Data/hora de criação |
| `atualizado_em` | DATETIME | Data/hora de atualização |

### Acesso Direto ao MySQL

```bash
# Acessar o container do MySQL
docker exec -it todolist-mysql mysql -u todouser -p

# Senha: todopass

# Comandos úteis
USE tododb;
SHOW TABLES;
SELECT * FROM todos;
```

---

## 🧪 Testando a Aplicação

### Teste Manual via Frontend

1. Acesse http://localhost
2. Adicione uma nova tarefa
3. Marque como concluída
4. Filtre por status
5. Exclua uma tarefa

### Teste via API (cURL)

```bash
# Criar tarefa
curl -X POST http://localhost:8080/api/v1/todos \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Teste","descricao":"Testando API"}'

# Listar tarefas
curl http://localhost:8080/api/v1/todos

# Alternar status (substitua {id} pelo ID real)
curl -X PATCH http://localhost:8080/api/v1/todos/1/toggle
```

### Teste com Postman ou Insomnia

Importe a coleção de endpoints e teste cada operação CRUD.

---

## 🐛 Resolução de Problemas

### Porta já em uso

Se receber erro de porta em uso:

```bash
# Verificar processos usando a porta
sudo lsof -i :8080
sudo lsof -i :3306
sudo lsof -i :80

# Matar processo específico
kill -9 [PID]

# Ou alterar as portas no docker-compose.yml
```

### Container não inicia

```bash
# Ver logs do container
docker-compose logs app
docker-compose logs mysql

# Reiniciar containers
docker-compose restart
```

### Banco de dados não conecta

```bash
# Verificar se o MySQL está rodando
docker-compose ps

# Testar conexão
docker exec -it todolist-mysql mysqladmin ping -h localhost
```

### Frontend não carrega

1. Verifique se o container frontend está rodando:
   ```bash
   docker-compose ps
   ```

2. Verifique os logs:
   ```bash
   docker-compose logs frontend
   ```

3. Acesse diretamente os arquivos em:
   `src/main/resources/static/index.html`

---

## 📝 Atividades Propostas para os Alunos

### Nível Básico

1. ✏️ Adicione um novo campo `prioridade` (Alta, Média, Baixa) à entidade Todo
2. 🎨 Customize as cores do frontend
3. 📋 Adicione um contador de tarefas pendentes

### Nível Intermediário

4. 🔍 Implemente um filtro de busca por descrição
5. 📅 Adicione um campo de data de vencimento
6. 🏷️ Implemente um sistema de tags/categorias

### Nível Avançado

7. 👤 Adicione autenticação com Spring Security
8. 📊 Crie um dashboard com estatísticas
9. 🔔 Implemente notificações para tarefas vencidas
10. 🌐 Adicione internacionalização (i18n)

---

## 📚 Referências e Material de Estudo

### Documentação Oficial

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Docker](https://docs.docker.com/)
- [MySQL](https://dev.mysql.com/doc/)

### Conceitos Importantes

- **REST:** Representational State Transfer
- **CRUD:** Create, Read, Update, Delete
- **JPA:** Java Persistence API
- **ORM:** Object-Relational Mapping
- **DTO:** Data Transfer Object
- **IoC:** Inversion of Control
- **DI:** Dependency Injection

---

## 👨‍🏫 Suporte

Para dúvidas sobre o projeto:

- **Professor:** Alysson
- **Instituição:** UNITINS
- **Site:** [unitins.br](https://unitins.br)
- **Programa:** TOGraduado

---

## 📄 Licença

Este projeto é de uso educacional para o curso de Computação Orientada a Serviços da UNITINS.

---

## 🎉 Conclusão

Este projeto demonstra como construir uma aplicação moderna usando microserviços, containerização e boas práticas de desenvolvimento. Use-o como base para seus próprios projetos e experimente adicionar novas funcionalidades!

**Bons estudos! 📚**

---

*Desenvolvido para fins didáticos - UNITINS TOGraduado*
