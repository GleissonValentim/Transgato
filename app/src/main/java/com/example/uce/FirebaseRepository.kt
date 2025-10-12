package com.example.uce

import androidx.room.util.copy
import com.google.firebase.database.FirebaseDatabase

object FirebaseRepository {
    private val db = FirebaseDatabase.getInstance().getReference()

    fun addManutencao(manutencao: Manutencao){
        val id = db.push().key!!
        db.child(id).setValue(manutencao.copy(id = id))
    }
}