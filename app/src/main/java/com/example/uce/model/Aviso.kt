package com.example.uce.model

data class Aviso(
    val id: String = "",
    val tituloAviso: String = "",
    val textoAviso: String = "",
    val data: Long = System.currentTimeMillis()
)
