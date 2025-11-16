package com.example.prova_3

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uce.R
import com.example.uce.navegation.Destinos
import com.example.uce.ui.theme.Principal
import com.example.uce.view.telaCaminhoneiro
import com.example.uce.viewmodel.ContasViewModel
import com.example.uce.viewmodel.ManutencaoViewModel

@Composable
fun telaLogin(
    navController: NavController, viewModel: ContasViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .background(Principal)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo do app",
            modifier = Modifier
                .width(270.dp)
                .height(270.dp)
                .padding(bottom = 50.dp),
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
                label = { Text("Digite o seu cpf") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("Digite o seu id") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current
            val status by viewModel.status.observeAsState("")
            val tipoConta by viewModel.tipoConta.observeAsState("")

            Button(
                onClick = {
                    viewModel.verificarConta(id.toIntOrNull() ?: -1, cpf)

                    when (status) {

                        "campos invalidos" -> {
                            Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                        }

                        "nao encontrado" -> {
                            Toast.makeText(context, "Usuário não encontrado!", Toast.LENGTH_SHORT).show()
                        }

                        "ok" -> {
                            when {
                                tipoConta.startsWith("Proprietario") ->
                                    navController.navigate(Destinos.telaProprietario.rota)

                                tipoConta.startsWith("Caminhoneiro") ->
                                    navController.navigate(Destinos.telaMotorista.rota)
                            }
                        }
                    }
                }
            ) {
                Text("Entrar")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLoginPreview() {
    val navController = rememberNavController()
    telaLogin(navController = navController)
}