package com.example.uce.view.TelasAdm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.example.uce.viewmodel.MainViewModel

@Composable
fun TelaGerenciarFuncionarios(viewModel: MainViewModel, navController: NavController) {
    val caminhoneiros by viewModel.listaDeCaminhoneiros.collectAsState()
    val carregar by viewModel.carregandoCaminhoneiros.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.carregarTodosCaminhoneiros()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        // Cabeçalho com "Voltar" à esquerda e Título centralizado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF10182D))
                .padding(24.dp)
        ) {
            Text(
                text = "Voltar",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { navController.popBackStack() }
            )

            Text(
                text = "Menu Proprietário",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gerenciamento de Funcionários",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (carregar) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF10182D))
            }
        } else {
            if (caminhoneiros.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Não há caminhoneiros cadastrados",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    )
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(caminhoneiros) { cami ->
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = cami.nome,
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.DarkGray
                                                )
                                            )
                                            Text(
                                                text = "CPF: ${cami.cpf}",
                                                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Editar",
                                                color = Color.Blue,
                                                fontSize = 16.sp,
                                                modifier = Modifier
                                                    .clickable { /* Editar */ }
                                                    .padding(8.dp)
                                            )

                                            Text(text = "|", color = Color.LightGray)

                                            Text(
                                                text = "Excluir",
                                                color = Color.Red,
                                                fontSize = 16.sp,
                                                modifier = Modifier
                                                    .clickable { /* Excluir */ }
                                                    .padding(8.dp)
                                            )
                                        }
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp,
                                        color = Color(0xFFEEEEEE)
                                    )
                                }
                            }
                        }
                    }


                    Button(
                        onClick = { /* Adicionar */ },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(24.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10182D))
                    ) {
                        Text("Adicionar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}