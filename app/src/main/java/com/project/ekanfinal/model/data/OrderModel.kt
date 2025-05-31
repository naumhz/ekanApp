package com.project.ekanfinal.model.data

data class OrderModel(
    val id: String = "",
    val address: AddressModel = AddressModel(),
    val status: String = "",
    val items: Map<String, Long> = emptyMap(),
    val total: Float = 0f,
)


