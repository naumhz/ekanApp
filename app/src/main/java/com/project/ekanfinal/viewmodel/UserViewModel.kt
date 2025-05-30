package com.project.ekanfinal.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
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

    //fungsi admin validasi
    fun validateCart(uid: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .update("isCartValidated", true)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Unknown error") }
    }

}
