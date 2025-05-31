package com.project.ekanfinal.model.data

import com.google.firebase.Timestamp

data class ReviewModel(
    val userId: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Timestamp? = null,
    val userName: String = "",
)

