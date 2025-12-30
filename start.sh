#!/bin/bash

###############################################################################
# Script de Inicialização - Todo List Microservice
# UNITINS TOGraduado - Prof. Alysson
###############################################################################

echo "=========================================="
echo "  Todo List Microservice - UNITINS"
echo "  Iniciando aplicação..."
echo "=========================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Função para verificar se Docker está instalado
check_docker() {
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}❌ Docker não está instalado!${NC}"
        echo "Por favor, instale o Docker: https://docs.docker.com/get-docker/"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker encontrado${NC}"
}

# Função para verificar se Docker Compose está instalado
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        echo -e "${RED}❌ Docker Compose não está instalado!${NC}"
        echo "Por favor, instale o Docker Compose: https://docs.docker.com/compose/install/"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker Compose encontrado${NC}"
}

# Função para verificar portas em uso
check_ports() {
    echo ""
    echo "Verificando portas necessárias..."

    if lsof -Pi :80 -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo -e "${YELLOW}⚠️  Porta 80 já está em uso${NC}"
        read -p "Deseja continuar mesmo assim? (s/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Ss]$ ]]; then
            exit 1
        fi
    fi

    if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo -e "${YELLOW}⚠️  Porta 8080 já está em uso${NC}"
        read -p "Deseja continuar mesmo assim? (s/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Ss]$ ]]; then
            exit 1
        fi
    fi

    if lsof -Pi :3306 -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo -e "${YELLOW}⚠️  Porta 3306 já está em uso${NC}"
        read -p "Deseja continuar mesmo assim? (s/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Ss]$ ]]; then
            exit 1
        fi
    fi

    echo -e "${GREEN}✅ Verificação de portas concluída${NC}"
}

# Função para limpar containers antigos
clean_old_containers() {
    echo ""
    echo "Limpando containers antigos..."
    docker-compose down 2>/dev/null
    echo -e "${GREEN}✅ Limpeza concluída${NC}"
}

# Função principal
main() {
    echo "1️⃣  Verificando dependências..."
    check_docker
    check_docker_compose

    echo ""
    echo "2️⃣  Verificando portas..."
    check_ports

    echo ""
    echo "3️⃣  Preparando ambiente..."
    clean_old_containers

    echo ""
    echo "4️⃣  Iniciando containers com Docker Compose..."
    echo -e "${YELLOW}Isso pode levar alguns minutos na primeira execução...${NC}"
    echo ""

    docker-compose up --build -d

    if [ $? -eq 0 ]; then
        echo ""
        echo -e "${GREEN}=========================================="
        echo "  ✅ Aplicação iniciada com sucesso!"
        echo "==========================================${NC}"
        echo ""
        echo "📱 Acesse a aplicação:"
        echo "   Frontend:     http://localhost"
        echo "   API REST:     http://localhost:8080/api/v1/todos"
        echo "   Health Check: http://localhost:8080/api/actuator/health"
        echo ""
        echo "📊 Verificar logs:"
        echo "   docker-compose logs -f"
        echo ""
        echo "🛑 Para parar a aplicação:"
        echo "   docker-compose down"
        echo ""
        echo -e "${YELLOW}Aguarde aproximadamente 30 segundos para todos os serviços iniciarem...${NC}"
        echo ""
    else
        echo -e "${RED}❌ Erro ao iniciar a aplicação${NC}"
        echo "Verifique os logs com: docker-compose logs"
        exit 1
    fi
}

# Executa função principal
main
