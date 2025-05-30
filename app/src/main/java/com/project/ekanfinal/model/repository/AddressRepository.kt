package com.project.ekanfinal.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.project.ekanfinal.model.data.AddressModel

class AddressRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getUserAddresses(
        userId: String,
        onComplete: (List<AddressModel>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("addresses")
            .get()
            .addOnSuccessListener { result ->
                val addresses = result.map { doc ->
                    doc.toObject(AddressModel::class.java)
                        .copy(id = doc.id)
                }
                onComplete(addresses)
            }
            .addOnFailureListener { onError(it) }
    }


    fun addUserAddress(
        userId: String,
        address: AddressModel,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("addresses")
            .add(address)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }
}