package com.project.ekanfinal.model.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ekanfinal.model.data.OrderModel
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.model.data.ReviewModel
import kotlinx.coroutines.tasks.await

class ReviewRepository {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()


    suspend fun getReviews(productId: String): List<ReviewModel> {
        val snapshot = firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .collection("reviews")
            .get()
            .await()

        return snapshot.toObjects(ReviewModel::class.java)
    }


    suspend fun getOrderById(uid: String, orderId: String): OrderModel {
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("orders")
            .document(orderId)
            .get()
            .await()

        return snapshot.toObject(OrderModel::class.java) ?: OrderModel()
    }

    suspend fun getProductsByIds(ids: List<String>): List<ProductModel> {
        if (ids.isEmpty()) return emptyList()

        val snapshot = firestore.collection("data")
            .document("stock")
            .collection("products")
            .whereIn("id", ids)
            .get()
            .await()

        return snapshot.toObjects(ProductModel::class.java)
    }

    suspend fun submitReview(productId: String, rating: Float, comment: String, orderId: String) {
        val uid = auth.currentUser?.uid ?: return
        val userDoc = firestore.collection("users").document(uid).get().await()
        val username = userDoc.getString("username") ?: ""
        val review = ReviewModel(
            userId = uid,
            rating = rating,
            comment = comment,
            timestamp = null,
            userName = username
        )
        firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .collection("reviews")
            .add(
                mapOf(
                    "userId" to review.userId,
                    "rating" to review.rating,
                    "comment" to review.comment,
                    "userName" to review.userName,
                    "timestamp" to FieldValue.serverTimestamp()
                )
            )
            .await()

        updateProductReviewSummary(productId)

        val globalOrderRef = firestore.collection("orders").document(orderId)
        val userOrderRef = firestore.collection("users").document(uid).collection("orders").document(orderId)

        firestore.runBatch { batch ->
            batch.update(globalOrderRef, "status", "reviewed")
            batch.update(userOrderRef, "status", "reviewed")
        }.await()
    }

    suspend fun updateProductReviewSummary(productId: String) {
        val reviewsSnapshot = firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .collection("reviews")
            .get()
            .await()

        val reviewCount = reviewsSnapshot.size()
        if (reviewCount == 0) {
            firestore.collection("data")
                .document("stock")
                .collection("products")
                .document(productId)
                .update(
                    mapOf(
                        "rating" to 0.0,
                        "reviewCount" to 0
                    )
                ).await()
            return
        }

        var totalRating = 0.0
        for (doc in reviewsSnapshot.documents) {
            val rating = doc.getDouble("rating") ?: 0.0
            totalRating += rating
        }

        val avgRating = totalRating / reviewCount

        firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .update(
                mapOf(
                    "rating" to avgRating,
                    "reviewCount" to reviewCount
                )
            ).await()
    }
}
