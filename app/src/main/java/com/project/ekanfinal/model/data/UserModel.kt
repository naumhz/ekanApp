package com.project.ekanfinal.model.data

data class UserModel(
    val username : String = "",
    val email : String = "",
    val uid : String = "",
    val cartItems : Map<String, Long> = emptyMap(),
    val address : String = "",
    val isCartValidated: Boolean = false
)
