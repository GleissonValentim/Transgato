package com.example.uce.view.TelasAdm.TelasManutencao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uce.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TelaManutencaoEscolhida(viewModel: MainViewModel, navController: NavController) {

    val manutencoes by viewModel.listaDeManutencoes.collectAsState()
    val motorista by viewModel.motoristaSelecionado.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F2F2))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF10182D))
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Manutenções",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = motorista?.nome ?: "Motorista",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                }
            }

            if (manutencoes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Nenhuma manutenção encontrada.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(manutencoes.sortedByDescending { it.data }) { manutencao ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = manutencao.tipo,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    )
                                    Text(
                                        text = "R$ ${manutencao.custo}",
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = manutencao.descricao, color = Color.DarkGray)

                                Spacer(modifier = Modifier.height(12.dp))

                                val formatado =
                                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                Text(
                                    text = formatado.format(manutencao.data),
                                    style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }
        }

        // Botão de voltar fixo no rodapé
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10182D))
            ) {
                Text("Voltar", color = Color.White)
            }
        }
    }
}