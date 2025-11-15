package com.example.uce.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uce.R
import com.example.uce.navegation.Destinos
import com.example.uce.ui.theme.Principal
import com.example.uce.ui.theme.UCETheme
import com.example.uce.viewmodel.LoginResult
import com.example.uce.viewmodel.MainViewModel

@Composable
fun telaLogin(
    navController: NavController,
    viewModel: MainViewModel = viewModel() // Usa o MainViewModel
) {
    // Observa o estado do login
    val loginState by viewModel.loginResult.collectAsState()
    val context = LocalContext.current

    // Reage às mudanças de estado do login
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginResult.Success -> {
                // Sucesso, navega para a tela correta
                Toast.makeText(context, "Bem-vindo, ${state.usuario.nome}!", Toast.LENGTH_SHORT).show()

                // LÓGICA DE NAVEGAÇÃO CORRIGIDA
                // Verifica o 'tipo' do usuário, não o 'id'
                if (state.usuario.tipo == "admin") {
                    // Se for admin, vai para a tela do proprietário
                    navController.navigate(Destinos.telaProprietario.rota) {
                        // Limpa a pilha de navegação para que o usuário não possa "voltar" para o login
                        popUpTo(Destinos.telaLogin.rota) { inclusive = true }
                    }
                } else {
                    // Se for motorista, vai para a tela do motorista
                    navController.navigate(Destinos.telaMotorista.rota) {
                        popUpTo(Destinos.telaLogin.rota) { inclusive = true }
                    }
                }

            }
            is LoginResult.Error -> {
                // Exibe a mensagem de erro vinda do ViewModel
                Toast.makeText(context, state.mensagem, Toast.LENGTH_LONG).show()
                viewModel.resetLoginState() // Reseta
            }
            // Não faz nada em Idle ou Loading (a UI cuida do Loading)
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize() // Preenche a tela inteira
            .background(Principal)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Top, // Alinha ao topo
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo do app",
            modifier = Modifier
                .width(200.dp) // Diminuí um pouco o logo
                .height(200.dp)
                .padding(bottom = 30.dp),
        )
        Column (
            modifier = Modifier
                .padding(30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ){
            var cpf by remember { mutableStateOf("") }
            var id by remember { mutableStateOf("") } // Mantemos o campo 'id', embora não seja mais obrigatório

            OutlinedTextField(
                value = cpf,
                onValueChange = { cpf = it },
                label = { Text("Digite o seu CPF") }, // Texto corrigido
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState != LoginResult.Loading // Desativa se estiver carregando
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("Digite o seu ID") }, // Texto corrigido
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState != LoginResult.Loading // Desativa se estiver carregando
            )

            Spacer(modifier = Modifier.height(20.dp))

            // LÓGICA DO BOTÃO CORRIGIDA
            Button(
                onClick = {
                    // Apenas chama a função do ViewModel.
                    // O LaunchedEffect vai cuidar da navegação/toast.
                    viewModel.fazerLogin(cpf, id)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), // Define uma altura padrão
                enabled = loginState != LoginResult.Loading // Desativa o botão se estiver carregando
            ) {
                if (loginState == LoginResult.Loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Entrar", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLoginPreview() {
    UCETheme {
        val navController = rememberNavController()
        telaLogin(navController = navController)
    }
}