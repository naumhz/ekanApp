package com.project.ekanfinal.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.project.ekanfinal.BottomNavigationBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.project.ekanfinal.model.data.UserModel


@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavHostController) {

    val user = remember {
        mutableStateOf(UserModel())

    }

    DisposableEffect(key1 = Unit) {
        var listener =  Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener{it, _ ->
                if (it!=null) {
                    val result = it.toObject(UserModel::class.java)
                    if (result != null) {
                        user.value = result
                    }
                }
            }
        onDispose {
            listener.remove()
        }
    }
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "Profile Page")
            Text(text = "Nama: ${user.value.username}")
            Text(text = "Email: ${user.value.email}")
            Text(text = "Alamat: ${user.value.address}")


            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login"){
                        popUpTo("profile"){
                            inclusive = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EADC9))
            ) {
                Text(text = "Keluar", color = Color.White)
            }

        }



    }
}