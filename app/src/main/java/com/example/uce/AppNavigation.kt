package com.example.composables

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composables.Routes.telaCaminhoneiro
import com.example.composables.Routes.telaLogin
import com.example.prova_3.TelaAdm
import com.example.prova_3.TelaLogin
import com.example.uce.telaCaminhoneiro

object Routes {
    const  val telaLogin = "telaLogin"
    const val TelaAdm = "TelaAdm"
    const val telaCaminhoneiro = "telaCaminhoneiro"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.telaLogin) {

        // Tela de login com os 2 botões
        composable(Routes.telaLogin) {
            TelaLogin(
                onLoginSuccess = {
                    navController.navigate(Routes.TelaAdm) {
                        popUpTo(Routes.telaLogin) { inclusive = false }
                    }
                },
                onCaminhoneiroSuccess = {
                    navController.navigate(Routes.telaCaminhoneiro) {
                        popUpTo(Routes.telaLogin) { inclusive = false }
                    }
                }
            )
        }

        // Tela Adm
        composable(Routes.TelaAdm) {
            TelaAdm()
        }

        // Tela Caminhoneiro
        composable(Routes.telaCaminhoneiro) {
            telaCaminhoneiro(navController = navController)
        }
    }
}

