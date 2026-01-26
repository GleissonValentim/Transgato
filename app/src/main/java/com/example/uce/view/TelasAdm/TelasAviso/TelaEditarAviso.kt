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
import com.example.uce.navegation.Destinos
import com.example.uce.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TelaEditarAviso(navController: NavController, viewModel: MainViewModel,
                    tituloTela: String, textoTela: String, dataTela: Long, idTela: String){
    var novoTitulo by remember { mutableStateOf(tituloTela) }
    var novoTexto by remember { mutableStateOf(textoTela) }

    val context = LocalContext.current
    val statusMesage by viewModel.statusMessage.collectAsState()

    LaunchedEffect(statusMesage) {
        statusMesage?.let {mensagem ->
            Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show()
            viewModel.onStatusMessageShown()
            if (mensagem.contains("editado")){
                navController.popBackStack()
            }else if (mensagem.contains("Erro")){

            }
        }
    }

    Column (
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF10182D))
                .padding(24.dp)
        ){
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
            text = "Editar Aviso ${tituloTela}",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card (
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column (modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = novoTitulo,
                    onValueChange = { novoTitulo = it },
                    label = { Text("Titulo novo...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = novoTexto,
                    onValueChange = { novoTexto = it },
                    label = { Text("Corpo do aviso novo...") },
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    supportingText = {
                        Text(
                            text = "${novoTexto.length} / 200",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            color = if (novoTexto.length >= 200) Color.Red else Color.Gray
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

            }

            Spacer(modifier = Modifier.height(20.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if (novoTitulo.isNotBlank() && novoTexto.isNotBlank()) {
                            viewModel.editarAviso(novoTitulo, novoTexto, dataTela, idTela)
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .height(70.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10182D))
                ) {
                    Text("Editar Aviso", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}