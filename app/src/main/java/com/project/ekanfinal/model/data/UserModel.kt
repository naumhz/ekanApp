package com.project.ekanfinal.model.data

import com.google.firebase.Timestamp


data class UserModel(
    val uid : String = "",
    val username : String = "",
    val email : String = "",
    val role : String = "user",
    val createdAt: Timestamp? = null,
    val cartItems : Map<String, Long> = emptyMap(),
//    val isCartValidated: Boolean = false
    val isOnline: Boolean = false,
    val lastSeen: Long? = null,
    val typingTo: String = ""
)
