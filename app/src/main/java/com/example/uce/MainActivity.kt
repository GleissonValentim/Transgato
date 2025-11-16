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
import com.example.prova_3.telaAdm
import com.example.prova_3.telaLogin
import com.example.uce.navegation.Destinos
import com.example.uce.ui.theme.UCETheme
import com.example.uce.view.TelaAddManutencao
import com.example.uce.view.telaCaminhoneiro
import com.example.uce.viewmodel.ContasViewModel
import com.example.uce.viewmodel.ManutencaoViewModel

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
        val manutencaoViewModel: ManutencaoViewModel = viewModel()
        val contasViewModel: ContasViewModel = viewModel()

        NavHost(
            navController = navController,
            startDestination = Destinos.telaLogin.rota
        ) {
            composable(Destinos.telaLogin.rota) {
                telaLogin(
                    navController = navController,
                    viewModel = contasViewModel
                )
            }

            composable(Destinos.telaMotorista.rota) {
                telaCaminhoneiro(
                    navController = navController,
                    viewModel = manutencaoViewModel
                )
            }

            composable(Destinos.telaProprietario.rota) {
                telaAdm(
                    navController = navController,
                    viewModel = manutencaoViewModel
                )
            }

            composable(Destinos.telaAddManutencao.rota) {
                TelaAddManutencao(
                    navController = navController,
                    viewModel = manutencaoViewModel
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
