package com.example.uce.view.TelasAdm.TelasGerenciarFuncionario

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

@Composable
fun TelaEditarMotorista(navController: NavController, viewModel: MainViewModel,
                        cpfTela: String,
                        nomeTela: String,
                        cnhTela: String,
                        idTela: String){
    var novoNome by remember { mutableStateOf(nomeTela) }
    var novoCpf by remember { mutableStateOf(cpfTela) }
    var novoCnh by remember { mutableStateOf(cnhTela) }
    var novoId by remember { mutableStateOf(idTela) }

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
    ) {
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
            text = "Editar Funcionário ${nomeTela}",
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
                    value = novoNome,
                    onValueChange = { novoNome = it },
                    label = { Text("Nome novo...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = novoCnh,
                    onValueChange = { novoCnh = it },
                    label = { Text("CNH nova...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = novoCpf,
                    onValueChange = { novoCpf = it },
                    label = { Text(novoCpf) },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = novoId,
                    onValueChange = { novoId = it },
                    label = { Text(novoId) },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if (novoNome.isNotBlank() && novoCpf.isNotBlank() && novoCnh.isNotBlank() && novoId.isNotBlank()) {
                            viewModel.editarCaminhoneiro(novoId, novoNome, novoCpf, novoCnh)
                            navController.navigate(Destinos.telaGerenciarFuncionarios.rota)
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
                    Text("Editar motorista", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

}