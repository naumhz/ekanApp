package com.project.ekanfinal.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.ekanfinal.GlobalNavigation.navController
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(pesan: String, modifier: Modifier = Modifier, viewmodel: UserViewModel = viewModel()) {

    val user by viewmodel.user

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Chat dengan Admin") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Pesan otomatis dari keranjang:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(pesan)
            Text("Nama: ${user?.username}")
            Text("Email: ${user?.email}")

        Button(
            onClick = {
                user?.uid?.let { uid ->
                    viewmodel.validateCart(
                        uid = uid,
                        onSuccess = {
                            Toast.makeText(context, "Cart divalidasi", Toast.LENGTH_SHORT).show()
                        },
                        onError = {
                            Toast.makeText(context, "Gagal: $it", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Hijau
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("VALIDASI Order", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)), // Merah
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("TIDAK VALIDASI Order", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
            Text("Status Validasi Sekarang: ${user?.isCartValidated}")
    } }
}
