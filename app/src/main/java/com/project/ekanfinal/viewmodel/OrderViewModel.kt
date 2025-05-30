package com.project.ekanfinal.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.model.data.UserModel
import com.project.ekanfinal.model.repository.ProductRepository
import com.project.ekanfinal.model.repository.UserRepository
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch


class OrderViewModel(
    private val userRepo: UserRepository = UserRepository(),
    private val productRepo: ProductRepository = ProductRepository()

) : ViewModel() {

    val user = mutableStateOf(UserModel())
    val productList = mutableStateListOf<ProductModel>()
    val total = mutableStateOf(0f)
    val ongkir = mutableStateOf(0f)
    val totalFinal = mutableStateOf(0f)

    val diskonMember = mutableStateOf(0f)
    val diskonOngkir = mutableStateOf(0f)
    val totalSetelahDiskon = mutableStateOf(0f)

    init {
        fetchCheckoutData()
    }

    fun ongkirPersen(): Float {
        return 0.1f
    }

    fun diskonMemberPersen(): Float {
        return 0.1f
    }

    fun diskonOngkirPersen(): Float {
        return 0.5f
    }


    private fun calculate() {
        total.value = productList.sumOf {
            val qty = user.value.cartItems[it.id] ?: 0
            ((it.harga.toFloatOrNull() ?: 0f) * qty).toDouble()
        }.toFloat()

        ongkir.value = total.value * ongkirPersen()

        diskonMember.value = total.value * diskonMemberPersen()

        diskonOngkir.value = ongkir.value * diskonOngkirPersen()

        totalFinal.value = total.value + ongkir.value

        totalSetelahDiskon.value = "%.2f".format(
            (totalFinal.value - diskonMember.value) + (ongkir.value - diskonOngkir.value)
        ).toFloat()
    }

    fun fetchCheckoutData() {
        viewModelScope.launch {
            val currentUser = userRepo.getCurrentUser()
            if (currentUser != null) {
                user.value = currentUser
                val products = productRepo.getProductsByIds(currentUser.cartItems.keys.toList())
                productList.clear()
                productList.addAll(products)
                calculate()
            }
        }
    }

    fun placeOrder(context: Context) {
        val userData = user.value
        val cartItems = userData.cartItems

        if (cartItems.isEmpty()) {
            Toast.makeText(context, "Keranjang kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        val orderData = hashMapOf(
            "uid" to userData.uid,
            "username" to userData.username,
            "email" to userData.email,
//            "address" to userData.address,
            "orderTime" to Timestamp.now(),
            "status" to "pending",
            "items" to cartItems
        )

        Firebase.firestore.collection("orders")
            .add(orderData)
            .addOnSuccessListener {
                // Kosongkan cart setelah order berhasil
                Firebase.firestore.collection("users").document(userData.uid)
                    .update("cartItems", emptyMap<String, Int>())
                    .addOnSuccessListener {
                        // Reset state di ViewModel
                        user.value = user.value.copy(cartItems = emptyMap())
                        productList.clear()
                        calculate()
                        Toast.makeText(context, "Pesanan berhasil dibuat!", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Gagal membuat pesanan!", Toast.LENGTH_SHORT).show()
            }
    }

}