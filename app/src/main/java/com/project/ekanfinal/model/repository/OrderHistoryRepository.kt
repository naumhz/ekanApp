package com.project.ekanfinal.model.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import com.project.ekanfinal.model.data.OrderModel
import com.project.ekanfinal.model.data.ProductModel

class OrderHistoryRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private var currentUid: String? = null

    fun setUserId(uid: String) {
        currentUid = uid
    }

    fun getAllOrders(onResult: (List<OrderModel>) -> Unit, onError: (Exception) -> Unit) {
        val uid = currentUid ?: run {
            onError(IllegalStateException("User ID belum diset"))
            return
        }
        db.collection("users")
            .document(uid)
            .collection("orders")
            .get()
            .addOnSuccessListener { snapshot ->
                val orders = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(OrderModel::class.java)?.copy(id = doc.id)
                }
                onResult(orders)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun updateOrderStatus(
        uid: String,
        orderId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userOrderRef = db.collection("users").document(uid).collection("orders").document(orderId)
        val globalOrderRef = db.collection("orders").document(orderId)

        val batch = db.batch()
        batch.update(userOrderRef, "status", newStatus)
        batch.update(globalOrderRef, "status", newStatus)

        batch.commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }


}
