package com.example.uce.view.TelasAdm.TelasAviso

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uce.viewmodel.MainViewModel

@Composable
fun TelaGerarAviso(viewModel: MainViewModel, navController: NavController){
    var titulo by remember { mutableStateOf("") }
    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current
    val statusMessage by viewModel.statusMessage.collectAsState()

    LaunchedEffect(statusMessage) {
        statusMessage?.let {mensagem ->
            Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
            viewModel.onStatusMessageShown()
            if (mensagem.contains("adicionado")){
                navController.popBackStack()
            }
        }
    }

    Column (modifier = Modifier.fillMaxWidth().background(Color(0xFFF2F2F2)))
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF10182D))
                .padding(vertical = 24.dp),
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
                style = TextStyle(
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título do aviso...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = texto,
                        onValueChange = { nTexto ->
                            if(nTexto.length <= 200){
                                texto = nTexto
                            }
                        },
                        label = { Text("Mensagem de aviso...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        maxLines = 10,
                        supportingText = {
                            Text(
                                text = "${texto.length} / 200",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                color = if (texto.length >= 200) Color.Red else Color.Gray
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if (titulo.isNotBlank() && texto.isNotBlank()) {
                            viewModel.salvarAviso(System.currentTimeMillis(), texto, titulo)
                        } else {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10182D))
                ) {
                    Text("Enviar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}