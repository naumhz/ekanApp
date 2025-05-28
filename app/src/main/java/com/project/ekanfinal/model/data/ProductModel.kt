package com.project.ekanfinal.model.data

data class ProductModel(
    val id: String = "",
    val kategori: String = "",
    val nama: String = "",
    val harga: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val deskripsi: String = "",
    val origin: String = "",
    val penyajian: String = "",
    val kondisi: String = "",
    val potongan: String = "",
    val imageUrl: String = ""
)
