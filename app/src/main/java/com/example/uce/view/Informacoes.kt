package com.example.uce.view

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.uce.navegation.Destinos

@Composable
fun Informacoes(navController: NavController, viewModel: ViewModel){
        val dicas = listOf(
            "Todos os dias antes de sair bater todos os pneus, conferir a água e o óleo.",
            "Não abuse da Velocidade.",
            "Regular os freios todas as semanas.",
            "Verificar as lonas de freio, molejo frequentemente.",
            "Não esquecer de escrever as manutenções do veículo.",
            "Limpar o filtro de ar e engraxar a cruzeta toda semana.",
            "Inverter as baterias a cada 3 meses.",
            "Engraxa o caminhão todo mês.",
            "Ligue os faróis durante o dia."
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F2)) // Fundo cinza claro
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF10182D))
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Voltar",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.clickable{
                        navController.popBackStack()
                    }
                )
                Text(
                    text = "Menu do Motorista",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Informações",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(dicas) { dica ->
                        Row(verticalAlignment = Alignment.Top) {
                            // Marcador (Bolinha azul)
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp, end = 12.dp)
                                    .size(10.dp)
                                    .background(Color(0xFF10182D), shape = CircleShape)
                            )
                            Text(
                                text = dica,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
