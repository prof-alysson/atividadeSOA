# ❓ Perguntas Frequentes (FAQ)

Respostas para dúvidas comuns dos alunos sobre o projeto Todo List.

---

## 📋 Índice

1. [Instalação e Configuração](#instalação-e-configuração)
2. [Execução do Projeto](#execução-do-projeto)
3. [Desenvolvimento](#desenvolvimento)
4. [Erros Comuns](#erros-comuns)
5. [Conceitos](#conceitos)

---

## Instalação e Configuração

### P: Preciso instalar Java e Maven para rodar o projeto?

**R:** Não! Se você usar Docker, não precisa instalar Java nem Maven. Tudo roda dentro dos containers. Basta ter Docker e Docker Compose instalados.

### P: Qual versão do Docker devo usar?

**R:** Qualquer versão recente do Docker (20.x ou superior) e Docker Compose (2.x ou superior) funcionará bem.

### P: Posso rodar o projeto sem Docker?

**R:** Sim, mas precisará:
- Java 17 instalado
- Maven instalado
- MySQL rodando localmente
- Ajustar as configurações em `application.yml`

### P: Como alterar as portas usadas pela aplicação?

**R:** Edite o arquivo `docker-compose.yml` e altere as portas na seção `ports`:

```yaml
ports:
  - "8080:8080"  # Altere o primeiro número (porta do host)
```

---

## Execução do Projeto

### P: Como sei se a aplicação está rodando corretamente?

**R:** Você pode verificar de três formas:

1. **Logs do Docker:**
   ```bash
   docker-compose logs -f app
   ```
   Procure por: "Aplicação iniciada com sucesso!"

2. **Health Check:**
   Acesse: http://localhost:8080/api/actuator/health
   Deve retornar: `{"status":"UP"}`

3. **Frontend:**
   Acesse: http://localhost
   O indicador no canto inferior direito deve mostrar "API Conectada"

### P: A aplicação demora muito para iniciar. É normal?

**R:** Sim! Na primeira execução:
- Download das imagens Docker: ~2-5 minutos
- Build da aplicação: ~3-5 minutos
- Nas próximas execuções será muito mais rápido (30-60 segundos)

### P: Como reiniciar apenas um container específico?

**R:** Use:
```bash
# Reiniciar apenas o backend
docker-compose restart app

# Reiniciar apenas o MySQL
docker-compose restart mysql

# Reiniciar apenas o frontend
docker-compose restart frontend
```

### P: Como ver os logs em tempo real?

**R:**
```bash
# Ver todos os logs
docker-compose logs -f

# Ver logs de um serviço específico
docker-compose logs -f app
docker-compose logs -f mysql
```

---

## Desenvolvimento

### P: Preciso rebuildar o Docker toda vez que altero o código?

**R:** Para alterações no **backend Java**, sim:
```bash
docker-compose up --build
```

Para alterações no **frontend** (HTML/CSS/JS), não precisa rebuildar, apenas recarregue a página do navegador.

### P: Como adicionar novos campos à tabela do banco de dados?

**R:** Basta adicionar o campo na entidade `Todo.java`. O Hibernate criará/atualizará a coluna automaticamente devido à configuração `ddl-auto: update`.

**Exemplo:**
```java
@Column(length = 50)
private String categoria;
```

### P: Como testar a API sem usar o frontend?

**R:** Você pode usar:

1. **cURL:**
   ```bash
   curl http://localhost:8080/api/v1/tarefas
   ```

2. **Postman ou Insomnia:**
   Ferramentas gráficas para testar APIs

3. **Navegador:**
   Para requisições GET, basta abrir a URL

### P: Como executar os testes unitários?

**R:** Se estiver usando Docker:
```bash
docker-compose exec app mvn test
```

Se tiver Maven instalado localmente:
```bash
mvn test
```

### P: Posso usar outro banco de dados em vez do MySQL?

**R:** Sim! Você pode usar PostgreSQL, H2, ou outros. Precisará:
1. Alterar a dependência no `pom.xml`
2. Ajustar a URL de conexão em `application.yml`
3. Mudar a imagem no `docker-compose.yml`

---

## Erros Comuns

### P: Erro "port is already allocated"

**R:** A porta já está em uso. Soluções:

1. **Identifique o processo:**
   ```bash
   # Linux/Mac
   lsof -i :8080

   # Windows
   netstat -ano | findstr :8080
   ```

2. **Mate o processo ou mude a porta no docker-compose.yml**

### P: Erro "Cannot connect to database"

**R:** Possíveis causas:

1. **MySQL ainda não iniciou completamente:**
   - Aguarde mais 30 segundos
   - Verifique: `docker-compose logs mysql`

2. **Credenciais incorretas:**
   - Verifique `DB_USER` e `DB_PASSWORD` no `application.yml`

3. **Container do MySQL não está rodando:**
   ```bash
   docker-compose ps
   ```

### P: Frontend carrega mas não mostra tarefas

**R:** Verifique:

1. **API está rodando?**
   - Acesse: http://localhost:8080/api/v1/tarefas

2. **CORS está configurado?**
   - Verifique `@CrossOrigin` no controller

3. **Console do navegador mostra erros?**
   - Pressione F12 e veja a aba Console

### P: Erro "Could not find or load main class"

**R:** O build do Maven falhou. Tente:

```bash
# Limpe e rebuilde
docker-compose down
docker-compose up --build --force-recreate
```

### P: Mudei o código mas não vejo as alterações

**R:**
1. **Backend:** Precisa rebuildar
   ```bash
   docker-compose up --build
   ```

2. **Frontend:** Limpe o cache do navegador (Ctrl+Shift+R ou Cmd+Shift+R)

---

## Conceitos

### P: O que é um microserviço?

**R:** É uma arquitetura onde a aplicação é dividida em serviços pequenos e independentes. Cada serviço:
- Tem sua própria base de dados
- Pode ser desenvolvido independentemente
- Comunica-se via APIs
- Pode ser escalado individualmente

### P: Por que usar Docker?

**R:** Docker garante que a aplicação rode da mesma forma em qualquer ambiente:
- "Funciona na minha máquina" deixa de ser problema
- Facilita deploy
- Isola dependências
- Simula ambiente de produção

### P: O que é REST?

**R:** REST (Representational State Transfer) é um estilo de arquitetura para APIs que usa:
- Verbos HTTP (GET, POST, PUT, DELETE)
- URLs como recursos (/todos)
- JSON para troca de dados
- Stateless (sem estado entre requisições)

### P: O que faz cada camada da aplicação?

**R:**
- **Controller:** Recebe requisições HTTP e retorna respostas
- **Service:** Contém lógica de negócio (regras da aplicação)
- **Repository:** Acessa o banco de dados
- **Model:** Define as entidades (estrutura dos dados)
- **DTO:** Transfere dados entre camadas

### P: Por que separar DTO de Model?

**R:**
- **Segurança:** Não expõe campos internos
- **Flexibilidade:** Frontend pode precisar de estrutura diferente
- **Validação:** Validações específicas por operação
- **Versionamento:** Facilita mudanças na API

### P: O que é JPA/Hibernate?

**R:**
- **JPA:** Especificação Java para mapeamento objeto-relacional
- **Hibernate:** Implementação do JPA
- **Benefício:** Escreve código em Java, Hibernate gera o SQL

### P: O que é Spring Boot Actuator?

**R:** Fornece endpoints para monitoramento e gerenciamento:
- `/actuator/health` - Verifica saúde da aplicação
- `/actuator/info` - Informações da aplicação
- `/actuator/metrics` - Métricas de performance

---

## 📞 Ainda tem dúvidas?

1. Revise a documentação no README.md
2. Consulte o GUIA_EXTENSAO.md
3. Verifique os comentários no código
4. Entre em contato com o professor

---

**Bons estudos! 📚**

*Prof. Alysson - UNITINS TOGraduado*
