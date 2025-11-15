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
import com.example.uce.ui.theme.UCETheme // Import para o Preview
import com.example.uce.viewmodel.MainViewModel // <-- CORREÇÃO: Importa o ViewModel correto
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TelaDoAdm( // <-- NOME CORRIGIDO
    navController: NavController,
    viewModel: MainViewModel = viewModel() // <-- CORREÇÃO: Pede o MainViewModel
) {
    // Coleta o estado da lista de manutenções do MainViewModel
    val manutencoes by viewModel.listaDeManutencoes.collectAsState()
    val context = LocalContext.current

    // Carrega as manutenções do Firebase
    LaunchedEffect(key1 = Unit) {
        viewModel.carregarTodasManutencoes()
    }

    // Observa mensagens de status
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

            if (manutencoes.isEmpty()) {
                Text(
                    text = "Não há manutenções no momento",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                )
            } else {
                LazyColumn(
                    // Ocupa o espaço disponível, menos o espaço do botão "Sair"
                    modifier = Modifier.padding(bottom = 80.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(manutencoes) { manutencao ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = manutencao.tipo, // <-- CORREÇÃO: Mostra o 'tipo'
                                    style = TextStyle(
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = manutencao.descricao, // Mostra a descrição
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Mostra o custo
                                val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                val custoFormatado = formatoMoeda.format(manutencao.custo)
                                Text("Custo: $custoFormatado")

                                Spacer(modifier = Modifier.height(5.dp))

                                // TODO: Você precisa buscar o nome do motorista
                                // usando o 'manutencao.caminhaoId'
                                Text(
                                    text = "Motorista: ${manutencao.caminhoneiroNome}",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Rodapé com o botão "Sair"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp), // Alinha o botão Sair na parte inferior
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    navController.navigate(Destinos.telaLogin.rota) {
                        // Limpa a pilha de navegação
                        popUpTo(Destinos.telaLogin.rota) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp) // Adiciona padding horizontal
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
        TelaDoAdm(navController = navController) // <-- CORREÇÃO: Nome da função no Preview
    }
}