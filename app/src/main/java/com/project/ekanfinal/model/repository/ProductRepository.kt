package com.project.ekanfinal.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.ekanfinal.model.data.ProductModel
import kotlinx.coroutines.tasks.await

//MODIF DONE
class ProductRepository {
    private val firestore = FirebaseFirestore.getInstance()
    val docRef = firestore.collection("data").document("stock").collection("products")

    suspend fun getProducts(): List<ProductModel> {
        return try {
            val snapshot = docRef.get().await()
            snapshot.documents.mapNotNull { it.toObject(ProductModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProductById(productId: String): ProductModel? {
        val detail = docRef.document(productId).get().await()
        return detail.toObject(ProductModel::class.java)
    }

    suspend fun getProductsByIds(ids: List<String>): List<ProductModel> {
        if (ids.isEmpty()) return emptyList()
        val snapshot = firestore.collection("data").document("stock")
            .collection("products")
            .whereIn("id", ids).get().await()
        return snapshot.toObjects(ProductModel::class.java)
    }


}