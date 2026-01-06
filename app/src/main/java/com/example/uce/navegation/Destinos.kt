package com.example.uce.navegation;

sealed class Destinos(val rota: String) {
    object telaMotorista : Destinos("tela_do_motorista")
    object telaProprietario : Destinos("tela_do_proprietario")
    object telaAddManutencao : Destinos("tela_de_adicionar_manutencao")
    object telaLogin : Destinos("tela_de_login")

    object telaInicialMotorista : Destinos("tela_inicial_motorista")
}
