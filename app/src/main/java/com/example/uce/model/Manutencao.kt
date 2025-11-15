package com.example.uce.model

import android.R

data class Manutencao(
    val id: String = "",
    val caminhaoId: String = "",
    val data: Long = 0L,
    val tipo: String = "",
    val descricao: String = "",
    val custo: Double = 0.0,
    val caminhoneiroNome: String = ""
)