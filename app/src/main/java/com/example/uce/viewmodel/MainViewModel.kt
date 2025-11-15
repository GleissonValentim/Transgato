package com.example.uce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uce.data.TransportadoraRepository
import com.example.uce.model.Caminhao
import com.example.uce.model.Caminhoneiro
import com.example.uce.model.Manutencao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginResult {
    object Idle : LoginResult()
    object Loading : LoginResult()
    data class Success(val usuario: Caminhoneiro) : LoginResult()
    data class Error(val mensagem: String) : LoginResult()
}

class MainViewModel : ViewModel() {

    private val repository = TransportadoraRepository()

    // --- ESTADOS DE LOGIN ---
    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> = _loginResult

    // --- ESTADOS DO USUÁRIO LOGADO ---
    // CORREÇÃO: Transforma o usuário logado num StateFlow para sobreviver a mudanças de estado
    private val _usuarioLogado = MutableStateFlow<Caminhoneiro?>(null)
    val usuarioLogado: StateFlow<Caminhoneiro?> = _usuarioLogado

    private val _caminhaoDoUsuario = MutableStateFlow<Caminhao?>(null)
    val caminhaoDoUsuario: StateFlow<Caminhao?> = _caminhaoDoUsuario

    // --- ESTADO DA LISTA DE MANUTENÇÕES (Usada pelo Admin E pelo Motorista) ---
    private val _listaDeManutencoes = MutableStateFlow<List<Manutencao>>(emptyList())
    val listaDeManutencoes: StateFlow<List<Manutencao>> = _listaDeManutencoes

    // --- ESTADO GERAL ---
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    /**
     * Tenta fazer o login buscando um usuário pelo CPF no Firebase.
     */
    fun fazerLogin(cpf: String, id: String) {
        viewModelScope.launch {
            _loginResult.value = LoginResult.Loading
            if (cpf.isBlank()) {
                _loginResult.value = LoginResult.Error("Por favor, preencha o CPF.")
                return@launch
            }

            val result = repository.getCaminhoneiroPorCpf(cpf)
            result.onSuccess { caminhoneiro ->
                if (caminhoneiro != null) {
                    // CORREÇÃO: Guarda o usuário no StateFlow
                    _usuarioLogado.value = caminhoneiro
                    _loginResult.value = LoginResult.Success(caminhoneiro)

                    if (caminhoneiro.tipo == "motorista") {
                        buscarCaminhaoEManutencoes(caminhoneiro.id)
                    }
                } else {
                    _loginResult.value = LoginResult.Error("Usuário não encontrado.")
                }
            }.onFailure { e ->
                _loginResult.value = LoginResult.Error("Erro ao conectar: ${e.message}")
            }
        }
    }

    /**
     * Reseta o estado de login para Idle (usado ao sair da tela, por ex).
     */
    fun resetLoginState() {
        _loginResult.value = LoginResult.Idle
        _usuarioLogado.value = null // CORREÇÃO: Limpa o StateFlow
        _caminhaoDoUsuario.value = null
        _listaDeManutencoes.value = emptyList()
    }

    /**
     * (PRIVADO) Busca o caminhão E DEPOIS as manutenções (Lógica encadeada).
     */
    private fun buscarCaminhaoEManutencoes(caminhoneiroId: String) {
        viewModelScope.launch {
            val resultCaminhao = repository.getCaminhaoPorCaminhoneiroId(caminhoneiroId)
            resultCaminhao.onSuccess { caminhao ->
                _caminhaoDoUsuario.value = caminhao

                if (caminhao == null) {
                    _statusMessage.value = "Atenção: Nenhum caminhão associado a este motorista."
                    _listaDeManutencoes.value = emptyList()
                } else {
                    // SUCESSO! Agora busca as manutenções
                    carregarManutencoes(caminhao.id)
                }
            }.onFailure { e ->
                _statusMessage.value = "Erro ao buscar caminhão: ${e.message}"
            }
        }
    }

    // Esta função é chamada pela TelaDoAdm
    fun carregarTodasManutencoes() {
        viewModelScope.launch {
            val result = repository.getAllManutencoes()
            result.onSuccess { lista ->
                _listaDeManutencoes.value = lista
            }.onFailure { e ->
                _statusMessage.value = "Erro ao carregar manutenções: ${e.message}"
            }
        }
    }

    // Função helper privada para carregar manutenções
    private fun carregarManutencoes(caminhaoId: String) {
        viewModelScope.launch {
            val result = repository.getManutencoesPorCaminhaoId(caminhaoId)
            result.onSuccess { lista ->
                _listaDeManutencoes.value = lista
            }.onFailure { e ->
                _statusMessage.value = "Erro ao carregar manutenções: ${e.message}"
            }
        }
    }

    /**
     * (Público) Recarrega as manutenções do motorista logado.
     */
    fun carregarManutencoesDoCaminhaoLogado() {
        val caminhaoId = _caminhaoDoUsuario.value?.id
        val userId = _usuarioLogado.value?.id // CORREÇÃO: Lê do StateFlow

        if (caminhaoId != null && caminhaoId.isNotBlank()) {
            carregarManutencoes(caminhaoId)
        } else if (userId != null && _usuarioLogado.value?.tipo == "motorista") {
            buscarCaminhaoEManutencoes(userId)
        }
    }

    /**
     * Cria um novo registro de manutenção e o salva no Firebase.
     */
    fun salvarManutencao(tipo: String, descricao: String, custo: Double) {
        viewModelScope.launch {

            var caminhaoIdAtual = _caminhaoDoUsuario.value?.id
            // CORREÇÃO: Lê o nome do StateFlow
            val nomeMotoristaAtual = _usuarioLogado.value?.nome

            // Se o caminhão ainda é nulo (devido à race condition),
            // tenta buscá-lo AGORA antes de falhar.
            if (caminhaoIdAtual == null || caminhaoIdAtual.isBlank()) {
                _statusMessage.value = "A confirmar dados do caminhão..."
                val userId = _usuarioLogado.value?.id // CORREÇÃO: Lê do StateFlow
                if (userId == null) {
                    _statusMessage.value = "Erro fatal: Usuário desapareceu."
                    return@launch
                }

                // Busca o caminhão de forma síncrona (await)
                val result = repository.getCaminhaoPorCaminhoneiroId(userId)
                if (result.isSuccess) {
                    val caminhaoEncontrado = result.getOrNull()
                    _caminhaoDoUsuario.value = caminhaoEncontrado
                    caminhaoIdAtual = caminhaoEncontrado?.id
                } else {
                    _statusMessage.value = "Erro: Falha ao confirmar caminhão."
                    return@launch
                }
            }

            // Verificação final (agora deve passar)
            if (caminhaoIdAtual == null || caminhaoIdAtual.isBlank() || nomeMotoristaAtual == null) {
                _statusMessage.value = "Erro: Não foi possível identificar o caminhão ou motorista."
                return@launch
            }

            val novaManutencao = Manutencao(
                caminhaoId = caminhaoIdAtual,
                data = System.currentTimeMillis(),
                tipo = tipo,
                descricao = descricao,
                custo = custo,
                caminhoneiroNome = nomeMotoristaAtual
            )
            val result = repository.addManutencao(novaManutencao)
            result.onSuccess {
                _statusMessage.value = "Manutenção registrada com sucesso!"
                // Atualiza a lista
                carregarManutencoes(caminhaoIdAtual)
            }.onFailure { e ->
                _statusMessage.value = "Erro ao registrar: ${e.message}"
            }
        }
    }

    /**
     * Limpa a mensagem de status após ela ser lida/exibida.
     */
    fun onStatusMessageShown() {
        _statusMessage.value = null
    }
}