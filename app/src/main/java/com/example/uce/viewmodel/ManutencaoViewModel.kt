package com.example.uce.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uce.model.Manutencao
import com.example.uce.model.RepositorioManutencao

class ManutencaoViewModel : ViewModel() {

    private val repositorio = RepositorioManutencao

    var tipo by mutableStateOf("")
        private set

    var descricao by mutableStateOf("")
        private set

    private val _manutencoes = mutableStateListOf<Manutencao>()
    val manutencoes: List<Manutencao> = _manutencoes

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    init {
        carregarManutencoes()
    }

    fun updateTipo(novoTipo: String) {
        tipo = novoTipo
    }

    fun updateDescricao(novaDescricao: String) {
        descricao = novaDescricao
    }

    fun carregarManutencoes() {
        _manutencoes.clear()
        _manutencoes.addAll(repositorio.buscarTodasManutencoes())
    }

    fun adicionarNovaManutencao(): String {
        if (tipo.isBlank() || descricao.isBlank()) {
            return "Preencha todos os campos para adicionar a manutenção."
        }

        repositorio.adicionarManutencao(tipo, descricao)
        carregarManutencoes()
        tipo = ""
        descricao = ""

        return "Manutenção '$tipo' adicionada com sucesso!"
    }

    fun listarManutencoes() {
        val manutencoes = repositorio.buscarTodasManutencoes()
        if (manutencoes.isEmpty()) {
            _toastMessage.value = "Nenhuma manutenção cadastrada."
            return
        }

        val listaFormatada = manutencoes.joinToString(separator = "\n--- Manutenção ---\n") {
            "ID: ${it.id}\nTipo: ${it.tipo}\nDescrição: ${it.descricao}"
        }

        _toastMessage.value = "--- Lista de Manutenções ---\n$listaFormatada"
    }
}
