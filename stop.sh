#!/bin/bash

###############################################################################
# Script de Parada - Todo List Microservice
# UNITINS TOGraduado - Prof. Alysson
###############################################################################

echo "=========================================="
echo "  Todo List Microservice - UNITINS"
echo "  Parando aplicação..."
echo "=========================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Pergunta se deseja remover volumes
echo -e "${YELLOW}Deseja remover os dados do banco de dados também? (s/n)${NC}"
read -p "> " -n 1 -r
echo

if [[ $REPLY =~ ^[Ss]$ ]]; then
    echo "Parando containers e removendo volumes..."
    docker-compose down -v
    echo -e "${GREEN}✅ Containers parados e dados removidos${NC}"
else
    echo "Parando containers (mantendo dados)..."
    docker-compose down
    echo -e "${GREEN}✅ Containers parados (dados preservados)${NC}"
fi

echo ""
echo "Para reiniciar, execute: ./start.sh"
echo ""
