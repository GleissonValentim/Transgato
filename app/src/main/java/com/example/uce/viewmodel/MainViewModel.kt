package com.example.uce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uce.data.TransportadoraRepository
import com.example.uce.model.Aviso
import com.example.uce.model.Caminhao
import com.example.uce.model.Caminhoneiro
import com.example.uce.model.Manutencao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class LoginResult {
    object Idle : LoginResult()
    object Loading : LoginResult()
    data class Success(val usuario: Caminhoneiro) : LoginResult()
    data class Error(val mensagem: String) : LoginResult()
}

class MainViewModel : ViewModel() {
    private val repository = TransportadoraRepository()

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)

    val loginResult: StateFlow<LoginResult> = _loginResult

    private val _usuarioLogado = MutableStateFlow<Caminhoneiro?>(null)

    val usuarioLogado: StateFlow<Caminhoneiro?> = _usuarioLogado

    private val _caminhaoDoUsuario = MutableStateFlow<Caminhao?>(null)

    val caminhaoDoUsuario: StateFlow<Caminhao?> = _caminhaoDoUsuario

    private val _listaDeManutencoes = MutableStateFlow<List<Manutencao>>(emptyList())

    val listaDeManutencoes: StateFlow<List<Manutencao>> = _listaDeManutencoes

    private val _listaDeCaminhoneiros = MutableStateFlow<List<Caminhoneiro>>(emptyList())

    val listaDeCaminhoneiros : StateFlow<List<Caminhoneiro>> = _listaDeCaminhoneiros

    private val _statusMessage = MutableStateFlow<String?>(null)

    val statusMessage: StateFlow<String?> = _statusMessage

    private val _motoristaSelecionado = MutableStateFlow<Caminhoneiro?>(null)
    val motoristaSelecionado: StateFlow<Caminhoneiro?> = _motoristaSelecionado

    fun fazerLogin(cpf: String, id: String) {
        viewModelScope.launch {
            _loginResult.value = LoginResult.Loading
            if (cpf.isBlank()) {
                _loginResult.value = LoginResult.Error("Por favor, preencha o CPF.")
                return@launch
            }

            if (id.isBlank()) {
                _loginResult.value = LoginResult.Error("Por favor, preencha o ID.")
                return@launch
            }

            val result = repository.getCaminhoneiroPorCpf(cpf)
            result.onSuccess { caminhoneiro ->
                if (caminhoneiro != null) {

                    if (caminhoneiro.id != id) {
                        _loginResult.value = LoginResult.Error("ID ou CPF incorretos. Por favor, tente novamente.")
                        return@onSuccess
                    }

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

    fun resetLoginState() {
        _loginResult.value = LoginResult.Idle
        _usuarioLogado.value = null
        _caminhaoDoUsuario.value = null
        _listaDeManutencoes.value = emptyList()
    }

    private fun buscarCaminhaoEManutencoes(caminhoneiroId: String) {
        viewModelScope.launch {
            val resultCaminhao = repository.getCaminhaoPorCaminhoneiroId(caminhoneiroId)
            resultCaminhao.onSuccess { caminhao ->
                _caminhaoDoUsuario.value = caminhao

                if (caminhao == null) {
                    _statusMessage.value = "Atenção: Nenhum caminhão associado a este motorista."
                    _listaDeManutencoes.value = emptyList()
                } else {

                    carregarManutencoes(caminhao.id)
                }
            }.onFailure { e ->
                _statusMessage.value = "Erro ao buscar caminhão: ${e.message}"
            }
        }
    }

    fun carregarTodosCaminhoneiros(){
        viewModelScope.launch {
            val result = repository.getAllCaminhoneiros()
            result.onSuccess { lista ->
                _listaDeCaminhoneiros.value = lista }.onFailure { e ->
                    _statusMessage.value = "Erro ao carregar caminhoneiros ${e.message}"
            }
        }
    }

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

    fun carregarManutencoesDoCaminhaoLogado() {
        val caminhaoId = _caminhaoDoUsuario.value?.id
        val userId = _usuarioLogado.value?.id

        if (caminhaoId != null && caminhaoId.isNotBlank()) {
            carregarManutencoes(caminhaoId)
        } else if (userId != null && _usuarioLogado.value?.tipo == "motorista") {
            buscarCaminhaoEManutencoes(userId)
        }
    }

    fun salvarManutencao(tipo: String, descricao: String, custo: Double) {
        viewModelScope.launch {

            var caminhaoIdAtual = _caminhaoDoUsuario.value?.id

            val nomeMotoristaAtual = _usuarioLogado.value?.nome

            if (caminhaoIdAtual == null || caminhaoIdAtual.isBlank()) {
                _statusMessage.value = "A confirmar dados do caminhão..."
                val userId = _usuarioLogado.value?.id
                if (userId == null) {
                    _statusMessage.value = "Erro fatal: Usuário desapareceu."
                    return@launch
                }

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

                carregarManutencoes(caminhaoIdAtual)
            }.onFailure { e ->
                _statusMessage.value = "Erro ao registrar: ${e.message}"
            }
        }
    }

    fun onStatusMessageShown() {
        _statusMessage.value = null
    }

    fun listarCaminhoneiros(){

    }


    //parte da tela inicial caminhoneiro
    private val _listaDeAvisos = MutableStateFlow<List<Aviso>>(emptyList())
    val listaDeAvisos: StateFlow<List<Aviso>> = _listaDeAvisos

    fun carregarAvisos(){
        viewModelScope.launch {
            val result = repository.getTodosAvisos()
            result.onSuccess { lista -> _listaDeAvisos.value = lista }
                    .onFailure { e -> _statusMessage.value = "Erro ao carregar: ${e.message}" }
        }
    }

    val avisoRecente: StateFlow<Aviso?> = _listaDeAvisos
        .map { it.lastOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)



    //parte da tela do adm

    fun carregarManutencaoEscolhida(motorista : Caminhoneiro){
        _motoristaSelecionado.value = motorista
        _listaDeManutencoes.value = emptyList()

        viewModelScope.launch {
            val resultadoBusca = repository.getCaminhaoPorCaminhoneiroId(motorista.id)

            resultadoBusca.onSuccess { caminhao ->
                if(caminhao != null){
                    carregarManutencoes(caminhao.id)
                }else{
                    _statusMessage.value = "Motorista não tem caminhão"
                }
            }
        }
    }
}