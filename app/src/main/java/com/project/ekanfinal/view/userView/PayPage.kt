package com.project.ekanfinal.view.userView

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.ekanfinal.R
import com.project.ekanfinal.StatusStepIndicator
import com.project.ekanfinal.viewmodel.OrderViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PayPage(
    navController: NavHostController,
    orderId: String,
    viewModel: OrderViewModel = viewModel()
) {

    val order by viewModel.selectedOrder.observeAsState()


    val scrollState = rememberScrollState()

    LaunchedEffect(orderId) {
        viewModel.loadOrderById(orderId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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
                text = "Detail Pesanan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2EADC9)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // STEP PEMBAYARAN
        StatusStepIndicator(currentStep = 1)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Text(
                text = "Pengiriman",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Alamat Pengiriman :",
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                order?.address?.let { Text(text = it.nama) }
                order?.address?.let { Text(text = it.noHp) }
                Text(
                    text = "${order?.address?.detailAlamat}, " +
                            "${order?.address?.kota}, " +
                            "${order?.address?.provinsi} " +
                            "${order?.address?.kodePos}"
                )
            }
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Text(
                text = "Rincian Pesanan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )

            Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                order?.let {
                    Text("Status: ${it.status.replaceFirstChar { it.uppercase() }}", fontSize = 16.sp)
                    Text("Total Produk: ${it.items.values.sum()}", fontSize = 16.sp)
                    Text("Total Harga: Rp ${it.total}", fontSize = 16.sp)
                }

            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        order?.let {
            Button(
                onClick = {
                    val total = it.total
                    navController.navigate("metode_pembayaran/$total/$orderId")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2EADC9),
                    contentColor = Color.White)
            ) {
                Text(text = "Bayar")
            }
        }


    }
}
