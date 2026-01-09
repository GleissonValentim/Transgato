package com.example.uce.navegation;

sealed class Destinos(val rota: String) {
    object telaManutencao : Destinos("tela_de_manutencao")
    object telaProprietario : Destinos("tela_do_proprietario")
    object telaAddManutencao : Destinos("tela_de_adicionar_manutencao")
    object telaLogin : Destinos("tela_de_login")
    object telaInicialMotorista : Destinos("tela_inicial_motorista")
    object telaInformacoes : Destinos("tela_informacoes")

    object telaTodosAvisos : Destinos("tela_todos_avisos")
}
