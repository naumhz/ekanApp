package com.project.ekanfinal.viewmodel

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.model.data.UserModel
import com.project.ekanfinal.model.repository.CartRepository
import com.project.ekanfinal.model.repository.DetailRepository
import com.project.ekanfinal.model.repository.ProductRepository
import com.project.ekanfinal.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository = CartRepository(),
    private val userRepository: UserRepository = UserRepository()

) : ViewModel() {

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()



    private val _products = mutableStateOf<Map<String, ProductModel>>(emptyMap())
    val product: State<Map<String, ProductModel>> = _products

    fun loadProduct(productId: String) {
        cartRepository.getProductById(productId) { product ->
            if (product != null) {
                _products.value = _products.value.toMutableMap().apply {
                    put(productId, product)
                }
            }
        }
    }



    fun addToCart(productId: String) {
        viewModelScope.launch {
            val success = cartRepository.addToCart(productId)
            if(success) {
                _toastMessage.emit("Berhasil Menambahkan Produk ke Keranjang")
            } else {
                _toastMessage.emit("Gagal Menambahkan Produk ke Keranjang")
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun RemoveFromCart(productId: String, removeAll : Boolean = false) {
        viewModelScope.launch {
            val success = cartRepository.RemoveFromCart(productId, removeAll)
            if(success) {
                if(removeAll)
                    _toastMessage.emit("Berhasil Menghapus Semua Produk dari Keranjang")
                else
                    _toastMessage.emit("Berhasil Mengurangi Produk dari Keranjang")
            } else {
                _toastMessage.emit("Gagal Mengurangi Produk dari Keranjang")
            }
        }
    }
}