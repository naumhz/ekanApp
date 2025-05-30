package com.project.ekanfinal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.ekanfinal.R

@Composable
fun OrderSuccessPage(navController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ordersuccess),
            contentDescription = null,
            modifier = Modifier
                .height(220.dp)
                .width(220.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Pesanan Berhasil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Terima kasih telah melakukan pemesanan. Pesanan Anda telah kami terima. Silahkan lanjutkan pembayaran untuk segera kami proses!",
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(0.85f),
            textAlign = TextAlign.Center
        )


        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("pesanan") {
                popUpTo("keranjang") { inclusive = true }
            } },
            modifier = Modifier.height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EADC9))
        ) {
            Text(text = "Lihat Pesanan Saya")
        }
    }
}

