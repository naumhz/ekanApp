package com.project.ekanfinal.view.userView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ekanfinal.R


@Composable
fun ProfilePage(navController: NavHostController) {

    val currentUser = FirebaseAuth.getInstance().currentUser
    val email = currentUser?.email ?: "Email not found"
    var displayName by remember { mutableStateOf("Memuat nama...") }
    val userId = currentUser?.uid

    LaunchedEffect(userId) {
        userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    displayName = document.getString("username") ?: "Nama tidak ditemukan"
                }
                .addOnFailureListener {
                    displayName = "Gagal memuat nama"
                }
        }
    }


    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2EADC9))
                    .padding(16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray, shape = CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = displayName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = email, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            ProfileMenuItem("Ubah Profil", R.drawable.ubahprofil) {
//                navController.navigate("edit_profile")
            }
            ProfileMenuItem("Alamat Pengiriman", R.drawable.alamatpeng) {
                navController.navigate("listAlamat")
            }
            ProfileMenuItem("Chat Admin", R.drawable.chatadmin) {  }
            ProfileMenuItem("About us", R.drawable.aboutus) {  }

            Spacer(modifier = Modifier.height(24.dp))

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

@Composable
fun ProfileMenuItem(title: String, iconRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = iconRes), contentDescription = title,
                modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 16.sp)
        }
    }
}


