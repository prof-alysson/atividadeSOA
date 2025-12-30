/**
 * Aplicação Frontend Todo List
 * Projeto Didático - UNITINS TOGraduado
 * Prof. Alysson
 */

// Endereço da API no servidor
const API_CONFIG = {
    baseURL: 'http://localhost:8080/api/v1/tarefas',
    headers: {
        'Content-Type': 'application/json'
    }
};

// Estado da aplicação
let currentFilter = 'all';
let tarefas = [];

// Funções para comunicar com o servidor (API)
const TarefaAPI = {
    // Busca todas as tarefas
    async getAll() {
        const response = await fetch(API_CONFIG.baseURL);
        if (!response.ok) throw new Error('Erro ao buscar tarefas');
        return await response.json();
    },

    // Cria uma nova tarefa
    async create(tarefaData) {
        const response = await fetch(API_CONFIG.baseURL, {
            method: 'POST',
            headers: API_CONFIG.headers,
            body: JSON.stringify(tarefaData)
        });
        if (!response.ok) throw new Error('Erro ao criar tarefa');
        return await response.json();
    },

    // Atualiza uma tarefa
    async update(id, tarefaData) {
        const response = await fetch(`${API_CONFIG.baseURL}/${id}`, {
            method: 'PUT',
            headers: API_CONFIG.headers,
            body: JSON.stringify(tarefaData)
        });
        if (!response.ok) throw new Error('Erro ao atualizar tarefa');
        return await response.json();
    },

    // Marca/desmarca como concluída
    async toggle(id) {
        const response = await fetch(`${API_CONFIG.baseURL}/${id}/toggle`, {
            method: 'PATCH',
            headers: API_CONFIG.headers
        });
        if (!response.ok) throw new Error('Erro ao alternar status');
        return await response.json();
    },

    // Deleta uma tarefa
    async delete(id) {
        const response = await fetch(`${API_CONFIG.baseURL}/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) throw new Error('Erro ao deletar tarefa');
    },

    // Verifica se a API está funcionando
    async checkHealth() {
        try {
            const response = await fetch('http://localhost:8080/api/actuator/health');
            return response.ok;
        } catch (error) {
            return false;
        }
    }
};

// Funções para atualizar a tela
const TarefaUI = {
    // Mostra as tarefas na tela
    renderTarefas(tarefasToRender) {
        const listaTarefas = document.getElementById('listaTarefas');

        if (tarefasToRender.length === 0) {
            listaTarefas.innerHTML = '<p class="empty-state">Nenhuma tarefa encontrada</p>';
            return;
        }

        listaTarefas.innerHTML = tarefasToRender.map(tarefa => `
            <div class="tarefa-item ${tarefa.concluida ? 'completed' : ''}" data-id="${tarefa.id}">
                <div class="tarefa-header">
                    <div style="display: flex; align-items: center; gap: 10px; flex: 1;">
                        <input
                            type="checkbox"
                            class="tarefa-checkbox"
                            ${tarefa.concluida ? 'checked' : ''}
                            onchange="handleAlternarTarefa(${tarefa.id})">
                        <h3 class="tarefa-title">${this.escapeHtml(tarefa.titulo)}</h3>
                    </div>
                    <div class="tarefa-actions">
                        <button class="btn btn-danger" onclick="handleDeletarTarefa(${tarefa.id})">
                            Excluir
                        </button>
                    </div>
                </div>
                ${tarefa.descricao ? `<p class="tarefa-description">${this.escapeHtml(tarefa.descricao)}</p>` : ''}
                <div class="tarefa-footer">
                    <span class="tarefa-date">Criado em: ${this.formatDate(tarefa.criadoEm)}</span>
                    <span class="tarefa-status ${tarefa.concluida ? 'completed' : 'pending'}">
                        ${tarefa.concluida ? 'Concluída' : 'Pendente'}
                    </span>
                </div>
            </div>
        `).join('');
    },

    // Protege contra código malicioso no texto
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    },

    // Formata a data para o padrão brasileiro
    formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    // Atualiza o indicador de conexão com a API
    updateAPIStatus(isConnected) {
        const statusElement = document.getElementById('apiStatus');
        const indicator = statusElement.querySelector('.status-indicator');
        const text = statusElement.querySelector('.status-text');

        if (isConnected) {
            indicator.classList.add('connected');
            text.textContent = 'API Conectada';
        } else {
            indicator.classList.remove('connected');
            text.textContent = 'API Desconectada';
        }
    },

    // Mostra mensagem de erro
    showError(message) {
        alert(`Erro: ${message}`);
    },

    // Mostra mensagem de sucesso
    showSuccess(message) {
        console.log(`Sucesso: ${message}`);
    }
};

// Filtra tarefas (todas, pendentes ou concluídas)
function filterTarefas() {
    let filtered = tarefas;

    if (currentFilter === 'pending') {
        filtered = tarefas.filter(tarefa => !tarefa.concluida);
    } else if (currentFilter === 'completed') {
        filtered = tarefas.filter(tarefa => tarefa.concluida);
    }

    TarefaUI.renderTarefas(filtered);
}

// Carrega as tarefas do servidor
async function carregarTarefas() {
    try {
        tarefas = await TarefaAPI.getAll();
        filterTarefas();
        TarefaUI.updateAPIStatus(true);
    } catch (error) {
        console.error('Erro ao carregar tarefas:', error);
        TarefaUI.showError('Não foi possível carregar as tarefas');
        TarefaUI.updateAPIStatus(false);
    }
}

// Função chamada quando o formulário é enviado
async function handleCriarTarefa(event) {
    event.preventDefault();

    const titulo = document.getElementById('titulo').value.trim();
    const descricao = document.getElementById('descricao').value.trim();

    if (!titulo) {
        TarefaUI.showError('O título é obrigatório');
        return;
    }

    try {
        const tarefaData = {
            titulo,
            descricao: descricao || null,
            concluida: false
        };

        await TarefaAPI.create(tarefaData);
        TarefaUI.showSuccess('Tarefa criada com sucesso');

        // Limpa o formulário
        document.getElementById('tarefaForm').reset();

        // Recarrega as tarefas
        await carregarTarefas();
    } catch (error) {
        console.error('Erro ao criar tarefa:', error);
        TarefaUI.showError('Não foi possível criar a tarefa');
    }
}

// Função chamada quando o checkbox é clicado
async function handleAlternarTarefa(id) {
    try {
        await TarefaAPI.toggle(id);
        await carregarTarefas();
        TarefaUI.showSuccess('Status atualizado');
    } catch (error) {
        console.error('Erro ao alternar status:', error);
        TarefaUI.showError('Não foi possível atualizar o status');
        await carregarTarefas(); // Reverte a mudança visual
    }
}

// Função chamada quando o botão excluir é clicado
async function handleDeletarTarefa(id) {
    if (!confirm('Tem certeza que deseja excluir esta tarefa?')) {
        return;
    }

    try {
        await TarefaAPI.delete(id);
        TarefaUI.showSuccess('Tarefa excluída com sucesso');
        await carregarTarefas();
    } catch (error) {
        console.error('Erro ao deletar tarefa:', error);
        TarefaUI.showError('Não foi possível excluir a tarefa');
    }
}

// Função chamada quando um botão de filtro é clicado
function handleFilterChange(filter) {
    currentFilter = filter;

    // Atualiza botões de filtro
    document.querySelectorAll('.btn-filter').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-filter="${filter}"]`).classList.add('active');

    filterTarefas();
}

// Verifica se a API está funcionando a cada 30 segundos
function startHealthCheck() {
    setInterval(async () => {
        const isHealthy = await TarefaAPI.checkHealth();
        TarefaUI.updateAPIStatus(isHealthy);
    }, 30000); // Verifica a cada 30 segundos
}

// Inicia a aplicação quando a página carregar
document.addEventListener('DOMContentLoaded', () => {
    console.log('Todo List App - UNITINS TOGraduado');
    console.log('Inicializando aplicação...');

    // Event Listeners
    document.getElementById('tarefaForm').addEventListener('submit', handleCriarTarefa);

    document.querySelectorAll('.btn-filter').forEach(btn => {
        btn.addEventListener('click', () => {
            handleFilterChange(btn.dataset.filter);
        });
    });

    // Carrega tarefas iniciais
    carregarTarefas();

    // Inicia verificação de saúde
    startHealthCheck();
});
