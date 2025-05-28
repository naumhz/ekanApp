package com.project.ekanfinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ekanfinal.model.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            if (result.isSuccess) {
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    fun register(username: String, email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.register(username, email, password)
            if (result.isSuccess) {
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.localizedMessage)
            }
        }
    }
}