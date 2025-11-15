package com.example.uce.data


import com.example.uce.model.Caminhao
import com.example.uce.model.Caminhoneiro
import com.example.uce.model.Manutencao
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * Repositório para gerenciar todas as operações de dados
 * com o Firebase Firestore.
 */
class TransportadoraRepository{

    // Obtém a instância do Firestore
    private val db: FirebaseFirestore = Firebase.firestore
    private val colecaoCaminhoneiros = db.collection("caminhoneiros")
    private val colecaoCaminhoes = db.collection("caminhoes")
    private val colecaoManutencoes = db.collection("manutencoes")

    /**
     * Adiciona um novo caminhoneiro ao Firestore.
     * Após adicionar, atualiza o documento com seu próprio ID.
     */
    suspend fun addCaminhoneiro(caminhoneiro: Caminhoneiro): Result<Unit> {
        return try {
            // Adiciona o documento e o Firestore gera um ID
            val docRef = colecaoCaminhoneiros.add(caminhoneiro).await()
            // Atualiza o documento recém-criado para incluir o campo 'id'
            docRef.update("id", docRef.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Adiciona um novo caminhão ao Firestore.
     * Após adicionar, atualiza o documento com seu próprio ID.
     */
    suspend fun addCaminhao(caminhao: Caminhao): Result<Unit> {
        return try {
            val docRef = colecaoCaminhoes.add(caminhao).await()
            docRef.update("id", docRef.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Adiciona um novo registro de manutenção ao Firestore.
     * Após adicionar, atualiza o documento com seu próprio ID.
     */
    suspend fun addManutencao(manutencao: Manutencao): Result<Unit> {
        return try {
            val docRef = colecaoManutencoes.add(manutencao).await()
            docRef.update("id", docRef.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca todas as manutenções (para a tela do Admin).
     */
    suspend fun getAllManutencoes(): Result<List<Manutencao>> {
        return try {
            val snapshot = colecaoManutencoes.get().await()
            // Converte os documentos do Firestore diretamente para a lista de data classes
            val manutencoes = snapshot.toObjects(Manutencao::class.java)
            Result.success(manutencoes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca um caminhoneiro pelo seu CPF para fazer o login.
     * @param cpf O CPF a ser buscado.
     * @return Result contendo o Caminhoneiro (se encontrado) ou null.
     */
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
                Result.success(null) // Este motorista não tem caminhão
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
}