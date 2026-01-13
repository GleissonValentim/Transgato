package com.example.uce.navegation;

sealed class Destinos(val rota: String) {
    object telaManutencao : Destinos("tela_de_manutencao")
    object telaAddManutencao : Destinos("tela_de_adicionar_manutencao")
    object telaLogin : Destinos("tela_de_login")
    object telaInicialMotorista : Destinos("tela_inicial_motorista")
    object telaInformacoes : Destinos("tela_informacoes")

    object telaTodosAvisos : Destinos("tela_todos_avisos")

    object telaManutencaoEscolhida : Destinos("tela_manutencao_escolhida")

    object telaGerarAviso: Destinos("tela_gerar_aviso")

    object telaInicialAdm: Destinos("tela_inicial_adm")

    object telaManutencaoAdm: Destinos("tela_manutencao_adm")
}
