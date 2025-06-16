package com.project.ekanfinal.view.userView

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.ekanfinal.R
import com.project.ekanfinal.viewmodel.OrderViewModel

@Composable
fun VoucherPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    orderViewModel: OrderViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.iconback),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF2EADC9)
                )
            }
            Text(
                text = "Voucher",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2EADC9)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        VoucherItem(
            title = "Diskon 10%",
            detail = "Untuk total belanja",
            voucherCode = "DISKON10",
            orderViewModel = orderViewModel,
            navController = navController
        )


        VoucherItem(
            title = "Gratis Ongkir",
            detail = "Diskon 100% ongkir",
            voucherCode = "ONGKIRFREE",
            orderViewModel = orderViewModel,
            navController = navController
        )

        // === VOUCHER MEMBER5 ===
        VoucherItem(
            title = "Diskon Member 5000",
            detail = "Potongan langsung Rp 5.000",
            voucherCode = "MEMBER5",
            orderViewModel = orderViewModel,
            navController = navController
        )
    }
}

@Composable
fun VoucherItem(
    title: String,
    detail: String,
    voucherCode: String,
    orderViewModel: OrderViewModel,
    navController: NavHostController
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .clickable {
                navController.popBackStack() // kembali ke halaman sebelumnya
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.voucher),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = detail, color = Color.Gray)
            }
        }
    }
}
