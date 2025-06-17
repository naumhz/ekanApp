package com.project.ekanfinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.model.repository.DetailRepository
import com.project.ekanfinal.model.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//MODIF DONE
class ProductViewModel(
    private val repository : ProductRepository = ProductRepository()
) : ViewModel() {

    private val _productList = MutableStateFlow<List<ProductModel>>(emptyList())
    val productList: StateFlow<List<ProductModel>> = _productList

    private val _selectedProduct = MutableStateFlow<ProductModel?>(null)
    val selectedProduct: StateFlow<ProductModel?> = _selectedProduct

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _productList.value = repository.getProducts()
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch {
            _selectedProduct.value = repository.getProductById(productId)
        }
    }
}