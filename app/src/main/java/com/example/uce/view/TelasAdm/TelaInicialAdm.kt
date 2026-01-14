package com.example.uce.view.TelasAdm

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun TelaIncialAdm(viewModel: MainViewModel, navController: NavController){
    Column (modifier = Modifier.fillMaxSize()
        .background(Color(0xFFF2F2F2))
    ) {
        Box(modifier = Modifier.fillMaxWidth()
            .background(Color(0xFF10182D))
            .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Menu Proprietário",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            //botões do Menu
            //falta a parte de mensagem
            Column (modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                MenuItemAdm(label = "Checar manutenções", onClick = {navController.navigate(Destinos.telaManutencaoAdm.rota)})
                MenuItemAdm(label = "Gerenciar Avisos", onClick = {navController.navigate(Destinos.telaGerenciarAvisos.rota)})
                MenuItemAdm(label = "Editar informações", onClick = {navController.navigate(Destinos.telaInformacoes.rota)})
                MenuItemAdm(label = "Gerenciar Funcionarios", onClick = {navController.navigate(Destinos.telaGerenciarFuncionarios.rota)})
                MenuItemAdm(label = "Mensagens", onClick = {Toast.makeText(context, "Funcionalidade ainda não existe", Toast.LENGTH_SHORT).show()})
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
fun MenuItemAdm(label: String, onClick: ()-> Unit){
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