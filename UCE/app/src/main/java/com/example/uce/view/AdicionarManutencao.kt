package com.example.uce.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uce.model.Manutencao
import com.example.uce.navegation.Destinos
import com.example.uce.ui.theme.Principal
import com.example.uce.viewmodel.ManutencaoViewModel

@Composable
fun TelaAddManutencao(navController: NavController, viewModel: ManutencaoViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Principal)
                    .padding(top = 25.dp)
                    .padding(bottom = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Menu do motorista",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    ),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Adicionar manutenção",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )

            val context = LocalContext.current

            val toastMessage by viewModel.toastMessage.observeAsState()

            LaunchedEffect(toastMessage) {
                toastMessage?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

            var tipo by remember { mutableStateOf("") }
            var descricao by remember { mutableStateOf("") }

            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo de manutenção") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.updateTipo(tipo)
                        viewModel.updateDescricao(descricao)
                        val mensagem = viewModel.adicionarNovaManutencao()

                        if (mensagem.startsWith("Preencha todos os campos para adicionar a manutenção")) {
                            Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
                            navController.navigate(Destinos.telaMotorista.rota)
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .padding(top = 5.dp, start = 270.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Principal
                    )
                ) {
                    Text("Adicionar")
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 550.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Principal)
                        .padding(top = 8.dp, bottom = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .padding(top = 20.dp, start = 250.dp)
                    ) {
                        Text("Voltar")
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 820.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Principal)
                    .padding(top = 8.dp, bottom = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .padding(top = 20.dp, start = 250.dp)
                ) {
                    Text("Voltar")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaAddManutencaoPreview() {
    val navController = rememberNavController()
    TelaAddManutencao(navController = navController)
}