package com.example.uce.model

object RepositorioManutencao {

    private val manutencoes = mutableListOf<Manutencao>()
    private var nextId = 1

    fun adicionarManutencao(tipo: String, descricao: String) {
        val novaManutencao = Manutencao(nextId++, tipo,
            descricao)
        manutencoes.add(novaManutencao)
    }

    fun buscarTodasManutencoes(): List<Manutencao> {
        return manutencoes.toList()
    }
}