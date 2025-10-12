package com.example.uce

import android.R
import android.app.Activity
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ink.geometry.Box
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uce.ui.theme.Principal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun telaCaminhoneiro(navController: NavController) {
    var mostrarFormulario by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(Principal)
                .padding(top = 25.dp)
                .padding(bottom = 25.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Menu do motorista",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                ),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Manutenções do veiculo",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )

        Button(
            onClick = {mostrarFormulario = !mostrarFormulario},
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Principal,
                contentColor = Color.White
            )
        ) {
            Text(if (mostrarFormulario) "Fechar Formulário" else "Adicionar Manutenção")
        }

        if (mostrarFormulario) {
            FormularioManutencao()
        }

        Spacer(modifier = Modifier.height(55.dp))

        val manutencoes = remember { mutableStateOf(listOf<Manutencao>()) }

        LazyColumn {
            items(manutencoes.value) { manutencao ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Tipo: ${manutencao.tipo}")
                        Text(text = "Descrição: ${manutencao.descricao}")
                    }
                }
            }
        }

        Text(
            text = "Não há manutenções no momento",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 480.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Principal)
                    .padding(top = 8.dp, bottom = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .padding(top = 20.dp, start = 250.dp)
                ) {
                    Text("Voltar")
                }
            }
        }
    }
}

@Composable
fun FormularioManutencao() {
    var tipo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = tipo,
            onValueChange = { tipo = it },
            label = { Text("Tipo de manutenção") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (!tipo.isNullOrBlank() && !descricao.isNullOrBlank()) {
                    FirebaseRepository.addManutencao(Manutencao("",tipo, descricao, ""))
                    Toast.makeText(
                        context,
                        "Manutenção adicionada com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Preencha todos os campos!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(top = 5.dp, start = 270.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Principal
                )
        ) {
            Text("Adicionar")
        }
    }
}

fun getManutencoes(onDataChar: (List<Manutencao>) -> Unit) {
    val db = FirebaseDatabase.getInstance().getReference("manutencoes")

    db.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val manutencaoList = mutableListOf<Manutencao>()
            snapshot.children.forEach {
                it.getValue(Manutencao::class.java)?.let { manutencao ->
                    manutencaoList.add(manutencao)
                }
            }
            onDataChar(manutencaoList)
        }

        override fun onCancelled(error: DatabaseError) {
            // Lide com erros aqui
        }
    })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaCaminhoneiroPreview() {
    val navController = rememberNavController()
    telaCaminhoneiro(navController = navController)
}