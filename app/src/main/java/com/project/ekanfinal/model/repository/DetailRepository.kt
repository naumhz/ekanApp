package com.project.ekanfinal.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.ekanfinal.model.data.ProductModel
import kotlinx.coroutines.tasks.await

class DetailRepository {
    private val firestore = FirebaseFirestore.getInstance()


    suspend fun getProductById(productID: String): ProductModel? {
        val docRef = firestore.collection("data").document("stock")
            .collection("products").document(productID)
            .get()
            .await()
        return docRef.toObject(ProductModel::class.java)
    }

}