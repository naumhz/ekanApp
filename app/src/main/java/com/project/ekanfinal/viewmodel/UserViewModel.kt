package com.project.ekanfinal.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.project.ekanfinal.model.data.UserModel
import com.project.ekanfinal.model.repository.UserRepository

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _user = mutableStateOf<UserModel?>(null)
    val user: State<UserModel?> = _user

    private var listenerRegistration: ListenerRegistration? = null

    init {
        startUserListener()
    }

    private fun startUserListener() {
        listenerRegistration = repository.getUserListener { newUser ->
            _user.value = newUser
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
