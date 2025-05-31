package com.project.ekanfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ekanfinal.model.data.OrderModel
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.model.data.ReviewModel
import com.project.ekanfinal.model.repository.ReviewRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReviewViewModel : ViewModel() {


    private val repository = ReviewRepository()

    private val _reviews = MutableLiveData<List<ReviewModel>>()
    val reviews: LiveData<List<ReviewModel>> = _reviews

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun loadReviews(productId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.getReviews(productId)
                _reviews.value = result
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Error getting reviews", e)
                _reviews.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products


    suspend fun loadProductsForOrder(uid: String, orderId: String): OrderModel {
        val order = repository.getOrderById(uid, orderId)
        val productIds = order.items.keys.toList()
        val productList = repository.getProductsByIds(productIds)
        _products.value = productList
        return order
    }


    fun submitAllReview(
        ratings: Map<String, Float>,
        comments: Map<String, String>,
        products: List<ProductModel>,
        orderId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ){
        viewModelScope.launch {
            try{
                val uid = repository.auth.currentUser?.uid ?: return@launch
                for(product in products){
                    val rating = ratings[product.id] ?: 0f
                    val comment = comments[product.id] ?: ""
                    repository.submitReview(product.id, rating, comment, orderId)

                }
                onSuccess()
            }catch (e: Exception){
                onError()
            }
        }
    }
}
