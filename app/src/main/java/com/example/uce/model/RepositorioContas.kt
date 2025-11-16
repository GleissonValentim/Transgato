package com.example.uce.model

object RepositorioContas {

    private val logins = mutableListOf<Login>()

    private val contas = mutableListOf<Login>(
        Login(1, "Gleisson",
            "11987654321", "Proprietario"),
        Login(2, "Caio",
            "21912345678", "Caminhoneiro")
    )

    fun buscarContas(id: Int, cpf: String): Login? {
        return contas.find { it.id == id && it.cpf == cpf }
    }
}