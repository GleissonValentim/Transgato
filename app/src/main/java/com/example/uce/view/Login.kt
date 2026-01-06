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
    viewModel: MainViewModel = viewModel()
) {
    val loginState by viewModel.loginResult.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginResult.Success -> {
                Toast.makeText(context, "Bem-vindo, ${state.usuario.nome}!", Toast.LENGTH_SHORT).show()

                if (state.usuario.tipo == "admin") {
                    navController.navigate(Destinos.telaProprietario.rota) {
                        popUpTo(Destinos.telaLogin.rota) { inclusive = true }
                    }
                } else {
                    navController.navigate(Destinos.telaInicialMotorista.rota) {
                        popUpTo(Destinos.telaLogin.rota) { inclusive = true }
                    }
                }

            }
            is LoginResult.Error -> {
                Toast.makeText(context, state.mensagem, Toast.LENGTH_LONG).show()
                viewModel.resetLoginState()
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Principal)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo do app",
            modifier = Modifier
                .width(200.dp)
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
            var id by remember { mutableStateOf("") }

            OutlinedTextField(
                value = cpf,
                onValueChange = { cpf = it },
                label = { Text("Digite o seu CPF") },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState != LoginResult.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("Digite o seu ID") },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState != LoginResult.Loading
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.fazerLogin(cpf, id)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = loginState != LoginResult.Loading
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