/**
 * Aplicação Frontend Todo List
 * Projeto Didático - UNITINS TOGraduado
 * Prof. Alysson
 *
 * Boas práticas aplicadas:
 * - Separação de responsabilidades (API, UI, Utils)
 * - Async/Await para operações assíncronas
 * - Tratamento de erros
 * - Código modular e reutilizável
 */

// Configuração da API
const API_CONFIG = {
    baseURL: 'http://localhost:8080/api/v1/todos',
    headers: {
        'Content-Type': 'application/json'
    }
};

// Estado da aplicação
let currentFilter = 'all';
let todos = [];

/**
 * API Service - Comunicação com o backend
 */
const TodoAPI = {
    /**
     * Lista todas as tarefas
     */
    async getAll() {
        const response = await fetch(API_CONFIG.baseURL);
        if (!response.ok) throw new Error('Erro ao buscar tarefas');
        return await response.json();
    },

    /**
     * Cria uma nova tarefa
     */
    async create(todoData) {
        const response = await fetch(API_CONFIG.baseURL, {
            method: 'POST',
            headers: API_CONFIG.headers,
            body: JSON.stringify(todoData)
        });
        if (!response.ok) throw new Error('Erro ao criar tarefa');
        return await response.json();
    },

    /**
     * Atualiza uma tarefa
     */
    async update(id, todoData) {
        const response = await fetch(`${API_CONFIG.baseURL}/${id}`, {
            method: 'PUT',
            headers: API_CONFIG.headers,
            body: JSON.stringify(todoData)
        });
        if (!response.ok) throw new Error('Erro ao atualizar tarefa');
        return await response.json();
    },

    /**
     * Alterna o status de conclusão
     */
    async toggle(id) {
        const response = await fetch(`${API_CONFIG.baseURL}/${id}/toggle`, {
            method: 'PATCH',
            headers: API_CONFIG.headers
        });
        if (!response.ok) throw new Error('Erro ao alternar status');
        return await response.json();
    },

    /**
     * Deleta uma tarefa
     */
    async delete(id) {
        const response = await fetch(`${API_CONFIG.baseURL}/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) throw new Error('Erro ao deletar tarefa');
    },

    /**
     * Verifica a saúde da API
     */
    async checkHealth() {
        try {
            const response = await fetch('http://localhost:8080/api/actuator/health');
            return response.ok;
        } catch (error) {
            return false;
        }
    }
};

/**
 * UI Service - Manipulação da interface
 */
const TodoUI = {
    /**
     * Renderiza a lista de tarefas
     */
    renderTodos(todosToRender) {
        const todoList = document.getElementById('todoList');

        if (todosToRender.length === 0) {
            todoList.innerHTML = '<p class="empty-state">Nenhuma tarefa encontrada</p>';
            return;
        }

        todoList.innerHTML = todosToRender.map(todo => `
            <div class="todo-item ${todo.concluida ? 'completed' : ''}" data-id="${todo.id}">
                <div class="todo-header">
                    <div style="display: flex; align-items: center; gap: 10px; flex: 1;">
                        <input
                            type="checkbox"
                            class="todo-checkbox"
                            ${todo.concluida ? 'checked' : ''}
                            onchange="handleToggleTodo(${todo.id})">
                        <h3 class="todo-title">${this.escapeHtml(todo.titulo)}</h3>
                    </div>
                    <div class="todo-actions">
                        <button class="btn btn-danger" onclick="handleDeleteTodo(${todo.id})">
                            Excluir
                        </button>
                    </div>
                </div>
                ${todo.descricao ? `<p class="todo-description">${this.escapeHtml(todo.descricao)}</p>` : ''}
                <div class="todo-footer">
                    <span class="todo-date">Criado em: ${this.formatDate(todo.criadoEm)}</span>
                    <span class="todo-status ${todo.concluida ? 'completed' : 'pending'}">
                        ${todo.concluida ? 'Concluída' : 'Pendente'}
                    </span>
                </div>
            </div>
        `).join('');
    },

    /**
     * Escapa HTML para prevenir XSS
     */
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    },

    /**
     * Formata data para exibição
     */
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

    /**
     * Atualiza status da API
     */
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

    /**
     * Mostra mensagem de erro
     */
    showError(message) {
        alert(`Erro: ${message}`);
    },

    /**
     * Mostra mensagem de sucesso
     */
    showSuccess(message) {
        console.log(`Sucesso: ${message}`);
    }
};

/**
 * Filtra as tarefas com base no filtro atual
 */
function filterTodos() {
    let filtered = todos;

    if (currentFilter === 'pending') {
        filtered = todos.filter(todo => !todo.concluida);
    } else if (currentFilter === 'completed') {
        filtered = todos.filter(todo => todo.concluida);
    }

    TodoUI.renderTodos(filtered);
}

/**
 * Carrega todas as tarefas
 */
async function loadTodos() {
    try {
        todos = await TodoAPI.getAll();
        filterTodos();
        TodoUI.updateAPIStatus(true);
    } catch (error) {
        console.error('Erro ao carregar tarefas:', error);
        TodoUI.showError('Não foi possível carregar as tarefas');
        TodoUI.updateAPIStatus(false);
    }
}

/**
 * Handler para criação de tarefa
 */
async function handleCreateTodo(event) {
    event.preventDefault();

    const titulo = document.getElementById('titulo').value.trim();
    const descricao = document.getElementById('descricao').value.trim();

    if (!titulo) {
        TodoUI.showError('O título é obrigatório');
        return;
    }

    try {
        const todoData = {
            titulo,
            descricao: descricao || null,
            concluida: false
        };

        await TodoAPI.create(todoData);
        TodoUI.showSuccess('Tarefa criada com sucesso');

        // Limpa o formulário
        document.getElementById('todoForm').reset();

        // Recarrega as tarefas
        await loadTodos();
    } catch (error) {
        console.error('Erro ao criar tarefa:', error);
        TodoUI.showError('Não foi possível criar a tarefa');
    }
}

/**
 * Handler para alternar status de conclusão
 */
async function handleToggleTodo(id) {
    try {
        await TodoAPI.toggle(id);
        await loadTodos();
        TodoUI.showSuccess('Status atualizado');
    } catch (error) {
        console.error('Erro ao alternar status:', error);
        TodoUI.showError('Não foi possível atualizar o status');
        await loadTodos(); // Reverte a mudança visual
    }
}

/**
 * Handler para deletar tarefa
 */
async function handleDeleteTodo(id) {
    if (!confirm('Tem certeza que deseja excluir esta tarefa?')) {
        return;
    }

    try {
        await TodoAPI.delete(id);
        TodoUI.showSuccess('Tarefa excluída com sucesso');
        await loadTodos();
    } catch (error) {
        console.error('Erro ao deletar tarefa:', error);
        TodoUI.showError('Não foi possível excluir a tarefa');
    }
}

/**
 * Handler para mudança de filtro
 */
function handleFilterChange(filter) {
    currentFilter = filter;

    // Atualiza botões de filtro
    document.querySelectorAll('.btn-filter').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-filter="${filter}"]`).classList.add('active');

    filterTodos();
}

/**
 * Verifica a saúde da API periodicamente
 */
function startHealthCheck() {
    setInterval(async () => {
        const isHealthy = await TodoAPI.checkHealth();
        TodoUI.updateAPIStatus(isHealthy);
    }, 30000); // Verifica a cada 30 segundos
}

/**
 * Inicialização da aplicação
 */
document.addEventListener('DOMContentLoaded', () => {
    console.log('Todo List App - UNITINS TOGraduado');
    console.log('Inicializando aplicação...');

    // Event Listeners
    document.getElementById('todoForm').addEventListener('submit', handleCreateTodo);

    document.querySelectorAll('.btn-filter').forEach(btn => {
        btn.addEventListener('click', () => {
            handleFilterChange(btn.dataset.filter);
        });
    });

    // Carrega tarefas iniciais
    loadTodos();

    // Inicia verificação de saúde
    startHealthCheck();
});
