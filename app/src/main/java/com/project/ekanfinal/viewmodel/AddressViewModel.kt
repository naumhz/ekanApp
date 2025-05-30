package com.project.ekanfinal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.project.ekanfinal.model.data.AddressModel
import com.project.ekanfinal.model.repository.AddressRepository

class AddressViewModel : ViewModel() {

    private val repo = AddressRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _addresses = MutableLiveData<List<AddressModel>>()
    val addresses:  LiveData<List<AddressModel>> get() = _addresses

    private fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun fetchAddresses() {
        val userId = getCurrentUserId()
        if (userId != null) {
            repo.getUserAddresses(
                userId,
                onComplete = { result ->
                    _addresses.value = result
                },
                onError = { e ->
                    _error.value = e.message
                }
            )
        } else {
            _error.value = "User belum login"
        }
    }

    fun addAddress(address: AddressModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = getCurrentUserId()
        if (userId != null) {
            repo.addUserAddress(
                userId,
                address,
                onSuccess = {
                    fetchAddresses()
                    onSuccess()
                },
                onError = { e -> onError(e.message ?: "Error tambah alamat") }
            )
        } else {
            onError("User belum login")
        }
    }

    private val _selectedAddress = MutableLiveData<AddressModel?>()
    val selectedAddress: LiveData<AddressModel?> get() = _selectedAddress

    fun selectAddress(address: AddressModel) {
        _selectedAddress.value = address
    }

}
