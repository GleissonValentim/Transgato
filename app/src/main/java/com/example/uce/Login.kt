package com.example.prova_3

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composables.Routes.telaLogin
import com.example.uce.R
import com.example.uce.telaCaminhoneiro
import com.example.uce.ui.theme.Principal

@Composable
fun TelaLogin(
    onLoginSuccess: () -> Unit,
    onCaminhoneiroSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Principal)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // substitua pelo nome do arquivo
            contentDescription = "Logo do app",
            modifier = Modifier
                .width(270.dp)
                .height(270.dp)
                .padding(bottom = 50.dp),
        )
        Column (
            modifier = Modifier
                .padding(30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ){
            var cpf by remember { mutableStateOf("") }
            var id by remember { mutableStateOf("") }

            OutlinedTextField(
                value = cpf,
                onValueChange = { cpf = it },
                label = { Text("Digite o seu cpf") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("Digite o seu id") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text("Tela do administrador")
            }

            Button(
                onClick = { onCaminhoneiroSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text("Tela do caminhoneiro")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLoginPreview() {
    TelaLogin(
        onLoginSuccess = {},
        onCaminhoneiroSuccess = {}
    )
}