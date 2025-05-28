package com.project.ekanfinal.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BannerRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val docRef = firestore.collection("data").document("banners")

    suspend fun getBanners(): List<String> {
        return try {
            val snapshot = docRef.get().await()
            snapshot.get("urls") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}