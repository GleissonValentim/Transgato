package com.example.uce.view.TelasAdm.TelasAviso

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun TelaGerenciarAvisos(viewModel: MainViewModel, navController: NavController) {
    val ultimoAviso by viewModel.avisoRecente.collectAsState()
    val avisos by viewModel.listaDeAvisos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.carregarAvisos();
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF10182D))
                .padding(vertical = 24.dp)
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
                text = "Menu Proprietário",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Último aviso",
                style = TextStyle(fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    if (ultimoAviso != null) {
                        Text(
                            text = ultimoAviso?.tituloAviso ?: "",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = ultimoAviso?.textoAviso ?: "",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Gray,
                                lineHeight = 22.sp
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val formatado = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            Text(
                                text = ultimoAviso?.data?.let { formatado.format(it) } ?: "",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = Color.LightGray
                                )
                            )
                        }
                    } else {
                        Text(
                            text = "Sem avisos",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ){
            LazyColumn (modifier = Modifier.fillMaxSize()){ items(avisos){ item ->
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = item?.tituloAviso ?: "",
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Text(
                            text = item?.textoAviso ?: "",
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = Color.Gray,
                                lineHeight = 22.sp
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val formatado = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            Text(
                                text = item?.data.let { formatado.format(it) } ?: "",
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    color = Color.LightGray
                                )
                            )
                        }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            }
        }

       // Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate(Destinos.telaGerarAviso.rota) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10182D))
        ) {
            Text("Gerar Novo Aviso", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}