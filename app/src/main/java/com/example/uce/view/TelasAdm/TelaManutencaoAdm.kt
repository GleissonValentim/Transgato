package com.example.uce.view.TelasAdm

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uce.navegation.Destinos
import com.example.uce.ui.theme.UCETheme
import com.example.uce.viewmodel.MainViewModel

@Composable
fun TelaManutencaoAdm(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {

    val caminhoneiros by viewModel.listaDeCaminhoneiros.collectAsState()
    val carregamento by viewModel.carregandoCaminhoneiros.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.carregarTodosCaminhoneiros()
    }

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF10182D))
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Menu do proprietario",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
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
            if(carregamento){
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF10182D))
                }
            }else{
                if (caminhoneiros.isEmpty()) {
                    Text(
                        text = "Não há caminhoneiros cadastrados no momento",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    )
                }else{
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
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
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
                                                text = "CPF: ${cam.cpf}",
                                                style = TextStyle(
                                                    fontSize = 15.sp,
                                                    color = Color.Gray
                                                )
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))
                                        }
                                        Column {
                                            Button(
                                                onClick = {
                                                    viewModel.carregarManutencaoEscolhida(cam)
                                                    navController.navigate(Destinos.telaManutencaoEscolhida.rota)
                                                },
                                                shape = RoundedCornerShape(8.dp),
                                                border = BorderStroke(
                                                    1.dp,
                                                    Color(0xFF5F849B)
                                                ),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent, //fundo transparente
                                                    contentColor = Color(0xFF5F849B) //cor azul do texto
                                                )
                                            ) { Text(text = "Verificar manutenções") }
                                        }
                                    }
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
                    navController.popBackStack()
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