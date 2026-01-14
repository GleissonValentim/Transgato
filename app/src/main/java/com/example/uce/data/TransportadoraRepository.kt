package com.example.uce.data


import com.example.uce.model.Aviso
import com.example.uce.model.Caminhao
import com.example.uce.model.Caminhoneiro
import com.example.uce.model.Manutencao
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.text.get

class TransportadoraRepository{

    private val db: FirebaseFirestore = Firebase.firestore
    private val colecaoCaminhoneiros = db.collection("caminhoneiros")
    private val colecaoCaminhoes = db.collection("caminhoes")
    private val colecaoManutencoes = db.collection("manutencoes")

    private val colecaoAvisos = db.collection("avisos")

    suspend fun addCaminhoneiro(caminhoneiro: Caminhoneiro): Result<Unit> {
        return try {
            val docRef = colecaoCaminhoneiros.add(caminhoneiro).await()

            docRef.update("id", docRef.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCaminhao(caminhao: Caminhao): Result<Unit> {
        return try {
            val docRef = colecaoCaminhoes.add(caminhao).await()
            docRef.update("id", docRef.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addManutencao(manutencao: Manutencao): Result<Unit> {
        return try {
            val docRef = colecaoManutencoes.add(manutencao).await()
            docRef.update("id", docRef.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllManutencoes(): Result<List<Manutencao>> {
        return try {
            val snapshot = colecaoManutencoes.get().await()

            val manutencoes = snapshot.toObjects(Manutencao::class.java)
            Result.success(manutencoes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllCaminhoneiros() : Result<List<Caminhoneiro>> {
        return try{
            val snapshot = colecaoCaminhoneiros.whereNotEqualTo("cpf", "admin").get().await()
            val caminhoneiros = snapshot.toObjects(Caminhoneiro::class.java)
            Result.success(caminhoneiros)
        } catch (e : Exception){
            Result.failure(e)
        }
    }

    suspend fun getCaminhoneiroPorCpf(cpf: String): Result<Caminhoneiro?> {
        return try {
            val snapshot = colecaoCaminhoneiros
                .whereEqualTo("cpf", cpf)
                .limit(1)
                .get()
                .await()

            if (snapshot.isEmpty) {
                Result.success(null)
            } else {
                val caminhoneiro = snapshot.documents.first().toObject(Caminhoneiro::class.java)
                Result.success(caminhoneiro)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCaminhaoPorCaminhoneiroId(caminhoneiroId: String): Result<Caminhao?> {
        return try {
            val snapshot = colecaoCaminhoes
                .whereEqualTo("caminhoneiroId", caminhoneiroId)
                .limit(1)
                .get()
                .await()

            if (snapshot.isEmpty) {
                Result.success(null)
            } else {
                val caminhao = snapshot.documents.first().toObject(Caminhao::class.java)
                Result.success(caminhao)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getManutencoesPorCaminhaoId(caminhaoId: String): Result<List<Manutencao>> {
        return try {
            val snapshot = colecaoManutencoes
                .whereEqualTo("caminhaoId", caminhaoId)
                .get()
                .await()
            val manutencoes = snapshot.toObjects(Manutencao::class.java)
            Result.success(manutencoes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTodosAvisos(): Result<List<Aviso>>{
        return try{
            val snapshot = colecaoAvisos.orderBy("data", Query.Direction.ASCENDING).get().await()
            val listaDeAvisos = snapshot.toObjects(Aviso::class.java)
            Result.success(listaDeAvisos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addAviso(aviso: Aviso): Result<Unit>{
        return try{
            val novoAviso = colecaoAvisos.add(aviso).await()
            novoAviso.update("id", novoAviso.id).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    fun getAvisoMaisRecente(onResult: (Aviso?) -> Unit): ListenerRegistration {
        return colecaoAvisos
            .orderBy("data", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onResult(null)
                    return@addSnapshotListener
                }
                val aviso = snapshot?.documents?.firstOrNull()?.toObject(Aviso::class.java)
                onResult(aviso)
            }
    }
}