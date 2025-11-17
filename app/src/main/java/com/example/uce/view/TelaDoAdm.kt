package com.example.uce.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.uce.navegation.Destinos
import com.example.uce.ui.theme.Principal
import com.example.uce.ui.theme.UCETheme
import com.example.uce.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TelaDoAdm(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {

    val manutencoes by viewModel.listaDeManutencoes.collectAsState()
    val caminhoneiros by viewModel.listaDeCaminhoneiros.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.carregarTodosCaminhoneiros()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.statusMessage.collect { message ->
            if (message != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.onStatusMessageShown()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Principal)
                    .padding(vertical = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Menu do proprietario",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    ),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Manutenções dos veiculos",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            if (caminhoneiros.isEmpty()) {
                Text(
                    text = "Não há caminhoneiros cadastrados no momento",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(bottom = 80.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(caminhoneiros) { cam ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = cam.nome,
                                    style = TextStyle(
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cam.cpf,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    viewModel.resetLoginState()
                    navController.navigate(Destinos.telaLogin.rota) {
                        popUpTo(Destinos.telaLogin.rota) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
            ) {
                Text("Sair")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaAdmPreview() {
    UCETheme {
        val navController = rememberNavController()
        TelaDoAdm(navController = navController)
    }
}