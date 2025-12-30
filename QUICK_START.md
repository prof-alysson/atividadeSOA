# ⚡ Quick Start Guide

Comece a usar o Todo List Microservice em **5 minutos**!

---

## 🎯 Pré-requisitos Mínimos

- ✅ Docker instalado ([Download](https://docs.docker.com/get-docker/))
- ✅ Docker Compose instalado ([Download](https://docs.docker.com/compose/install/))
- ✅ Portas 80, 3306 e 8080 disponíveis

---

## 🚀 Iniciar o Projeto

### Opção 1: Script Automático (Recomendado)

```bash
# Linux/Mac
./start.sh

# Windows (Git Bash)
bash start.sh
```

### Opção 2: Manual

```bash
docker-compose up --build
```

---

## ⏱️ Aguarde a Inicialização

**Primeira vez:** 5-10 minutos (download de imagens + build)
**Próximas vezes:** 30-60 segundos

Você verá:
```
Todo List Microservice - UNITINS
Aplicação iniciada com sucesso!
```

---

## 🌐 Acessar a Aplicação

| Serviço | URL | Descrição |
|---------|-----|-----------|
| **Frontend** | http://localhost | Interface web |
| **API** | http://localhost:8080/api/v1/todos | API REST |
| **Health** | http://localhost:8080/api/actuator/health | Status |

---

## ✅ Verificar se Está Funcionando

### 1. Abra o Frontend
```
http://localhost
```
✓ Deve ver "API Conectada" no canto inferior direito

### 2. Teste a API
```bash
curl http://localhost:8080/api/v1/todos
```
✓ Deve retornar `[]` (lista vazia)

### 3. Crie uma Tarefa
No frontend:
1. Digite um título
2. Clique em "Adicionar Tarefa"
3. Veja a tarefa aparecer na lista

---

## 🎮 Comandos Úteis

```bash
# Ver logs
docker-compose logs -f

# Parar aplicação
docker-compose down

# Parar e limpar dados
docker-compose down -v

# Reiniciar
docker-compose restart

# Ver status
docker-compose ps
```

---

## 🐛 Problemas?

### Porta em uso
```bash
# Identifique o processo
lsof -i :8080

# Mate o processo
kill -9 [PID]
```

### API não conecta
```bash
# Verifique os logs
docker-compose logs app

# Aguarde mais tempo (primeira vez demora)
```

### Frontend não carrega
```bash
# Verifique se todos os serviços estão up
docker-compose ps

# Todos devem mostrar "Up"
```

---

## 📚 Próximos Passos

1. ✅ Aplicação rodando? Parabéns!
2. 📖 Leia o [README.md](README.md) completo
3. 🎓 Explore o [GUIA_EXTENSAO.md](GUIA_EXTENSAO.md)
4. ❓ Dúvidas? Veja o [FAQ.md](FAQ.md)
5. 🏗️ Arquitetura? Leia [ARQUITETURA.md](ARQUITETURA.md)

---

## 🎉 Pronto!

Você agora tem um microserviço completo rodando!

**Explore, aprenda e divirta-se!** 🚀

---

*Prof. Alysson - UNITINS TOGraduado*
