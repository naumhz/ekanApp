package com.project.ekanfinal.model.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.project.ekanfinal.model.data.ProductModel
import kotlinx.coroutines.tasks.await

class CartRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()



    suspend fun addToCart(productId: String): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        val userDoc = firestore.collection("users").document(uid)

        return try {
            val snapshot = userDoc.get().await()
            val currentCart = snapshot.get("cartItems") as? Map<String, Long> ?: emptyMap()
            val currentQuantity = currentCart[productId] ?: 0
            val updatedQuantity = currentQuantity + 1
            val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)

            userDoc.update(updatedCart).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun RemoveFromCart(productId: String, removeAll : Boolean = false): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        val userDoc = firestore.collection("users").document(uid)

        return try {
            val snapshot = userDoc.get().await()
            val currentCart = snapshot.get("cartItems") as? Map<String, Long> ?: emptyMap()
            val currentQuantity = currentCart[productId] ?: 0
            val updatedQuantity = currentQuantity - 1

            val updatedCart=
                if (updatedQuantity <= 0 || removeAll)
                    mapOf("cartItems.$productId" to FieldValue.delete())
                else
                    mapOf("cartItems.$productId" to updatedQuantity)

            userDoc.update(updatedCart).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getProductById(productId: String, onResult: (ProductModel?) -> Unit) {
        firestore.collection("data").document("stock")
            .collection("products").document(productId)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(ProductModel::class.java)
                onResult(product)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}