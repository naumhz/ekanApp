package com.project.ekanfinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ekanfinal.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val repository: UserRepository = UserRepository()

    val username = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    val usernameError = MutableStateFlow<String?>(null)
    val emailError = MutableStateFlow<String?>(null)
    val passwordError = MutableStateFlow<String?>(null)
    val confirmPasswordError = MutableStateFlow<String?>(null)

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole : StateFlow<String?> get() = _userRole

    private val _registerSuccess = MutableStateFlow<Boolean?>(null)
    val registerSuccess : StateFlow<Boolean?> get() = _registerSuccess

    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess : StateFlow<Boolean?> get() = _loginSuccess

    private val _generalError = MutableStateFlow<String?>(null)
    val generalError : StateFlow<String?> get() = _generalError

    private val _isLoading = MutableStateFlow<Boolean?>(null)
    val isLoading : StateFlow<Boolean?> get() = _isLoading

    private val _currentUsername = MutableStateFlow<String?>(null)
    val currentUsername : StateFlow<String?> get() = _currentUsername

    private val _currentEmail = MutableStateFlow<String?>(null)
    val currentEmail : StateFlow<String?> get() = _currentEmail

    init{

    }

    //CHECK FIELD TERISI SEMUA, KALAU ADA YANG KOSONG MENAMPILKAN ERROR
    fun onUsernameChanged(value: String){
        username.value = value
        if (value.isNotBlank()) usernameError.value = null
    }

    fun onEmailChanged(value: String){
        email.value = value
        if (value.isNotBlank()) emailError.value = null
    }

    fun onPasswordChanged(value: String){
        password.value = value
        if (value.isNotBlank()) passwordError.value = null
    }

    fun onConfirmPasswordChanged(value: String){
        confirmPassword.value = value
        if (value.isNotBlank()) confirmPasswordError.value = null
    }

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