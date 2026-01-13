package com.example.uce.view.TelasMotorista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uce.navegation.Destinos
import com.example.uce.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TelaInicialCaminhoneiro(navController: NavController, viewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        viewModel.carregarAvisoRecente()
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFF2F2F2)) // Fundo cinza claro
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF10182D))
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Menu do Motorista",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            //avisos
                AvisosCard(navController, viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            //botões do Menu
            //fazer a tela de cada um e colocar aqui depois
            //falta a parte de mensagem
            Column (modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                MenuItem(label = "Manutenções do veículo", onClick = {navController.navigate(Destinos.telaManutencao.rota)})
                MenuItem(label = "Mensagem para Proprietário", onClick = {navController.navigate(Destinos.telaManutencao.rota)})
                MenuItem(label = "Mensagem para Funcionários", onClick = {navController.navigate(Destinos.telaManutencao.rota)})
                MenuItem(label = "Caixa de Mensagens", onClick = {navController.navigate(Destinos.telaManutencao.rota)})
                MenuItem(label = "Informações", onClick = {navController.navigate(Destinos.telaInformacoes.rota)})
            }


            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF10182D))
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                onClick = {
                viewModel.resetLoginState()
                navController.navigate(Destinos.telaLogin.rota)
                          },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD31C1C)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Sair", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AvisosCard(navController: NavController, viewModel: MainViewModel) {
    val ultimoAviso by viewModel.avisoRecente.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Avisos", style = TextStyle(
                color = Color.Red,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = ultimoAviso?.tituloAviso ?: "Sem título",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    val formatado = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    Text(
                        text = ultimoAviso?.data?.let { horaCerta -> formatado.format(horaCerta) } ?: "data Indisponivel",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = TextStyle(fontSize = 12.sp, color = Color.Gray)
                    )
                }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate(Destinos.telaTodosAvisos.rota) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10182D)),
                shape = RoundedCornerShape(50)
            ) {
                Text("Ver todos os avisos", color = Color.White)
                //fazer função para ver avisos
            }
        }
    }
}

@Composable
fun MenuItem(label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(70.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10182D)
            )
        }
    }
}