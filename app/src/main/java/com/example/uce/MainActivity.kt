package com.example.uce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



import com.example.uce.navegation.Destinos
import com.example.uce.ui.theme.UCETheme
import com.example.uce.view.Informacoes
import com.example.uce.view.TelasMotorista.TelaAddManutencao
import com.example.uce.view.TelasAdm.TelaManutencaoAdm
import com.example.uce.view.TelasAdm.TelaGerarAviso
import com.example.uce.view.TelasAdm.TelaGerenciarAvisos
import com.example.uce.view.TelasAdm.TelaGerenciarFuncionarios
import com.example.uce.view.TelasAdm.TelaIncialAdm
import com.example.uce.view.TelasAdm.TelaManutencaoEscolhida
import com.example.uce.view.TelasMotorista.TelaInicialCaminhoneiro
import com.example.uce.view.TelasMotorista.TelaManutencao
import com.example.uce.view.TelasMotorista.TelaTodosAvisos
import com.example.uce.view.telaLogin
import com.example.uce.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UCETheme {
                Transgato()
            }
        }
    }
}

@Composable
fun Transgato() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        val mainViewModel : MainViewModel = viewModel()

        NavHost(
            navController = navController,
            startDestination = Destinos.telaLogin.rota
        ) {
            composable(Destinos.telaLogin.rota) {
                telaLogin(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable(Destinos.telaManutencao.rota) {
                TelaManutencao(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable(Destinos.telaInicialMotorista.rota){
                TelaInicialCaminhoneiro(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable(Destinos.telaInformacoes.rota){
                Informacoes(
                    navController,
                    mainViewModel
                )
            }

            composable(Destinos.telaAddManutencao.rota) {
                TelaAddManutencao(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable (Destinos.telaTodosAvisos.rota) {
                TelaTodosAvisos(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable (Destinos.telaManutencaoEscolhida.rota) {
                TelaManutencaoEscolhida(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable (Destinos.telaGerarAviso.rota) {
                TelaGerarAviso(navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable (Destinos.telaInicialAdm.rota) {
                TelaIncialAdm(navController = navController,
                    viewModel = mainViewModel)
            }

            composable(Destinos.telaManutencaoAdm.rota) {
                TelaManutencaoAdm(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable (Destinos.telaGerenciarAvisos.rota) {
                TelaGerenciarAvisos(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }

            composable(Destinos.telaGerenciarFuncionarios.rota){
                TelaGerenciarFuncionarios(
                    navController = navController,
                    viewModel = mainViewModel
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UCETheme {
        Transgato()
    }
}
