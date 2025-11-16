package com.example.uce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uce.model.RepositorioContas

class ContasViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _tipoConta = MutableLiveData<String>()
    val tipoConta: LiveData<String> = _tipoConta

    fun verificarConta(id: Int, cpf: String) {

        if (id <= 0 || cpf.isBlank()) {
            _status.value = "campos invalidos"
            return
        }

        val conta = RepositorioContas.buscarContas(id, cpf)

        if (conta != null) {
            _tipoConta.value = conta.tipo
            _status.value = "ok"
        } else {
            _status.value = "nao encontrado"
        }
    }
}