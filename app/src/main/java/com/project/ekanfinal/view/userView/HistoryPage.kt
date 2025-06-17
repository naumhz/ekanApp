package com.project.ekanfinal.view.userView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ekanfinal.viewmodel.OrderViewModel

@Composable
fun HistoryPage(
    navController: NavHostController,
    uid: String,
    viewModel: OrderViewModel = viewModel()
) {
    LaunchedEffect(uid) {
        viewModel.setUser(uid)
    }

    val orders by viewModel.orders.observeAsState(emptyList())
    val error by viewModel.error.observeAsState("")


    var selectedStatus by remember { mutableStateOf("Semua") }
    val statusOptions = listOf("Semua", "Belum Bayar", "Diproses", "Dikirim", "Selesai", "Reviewed")

    val statusPriority = mapOf(
        "Belum Bayar" to 0,
        "Diproses" to 1,
        "Dikirim" to 2,
        "Selesai" to 3,
        "Reviewed" to 4
    )

    val filteredOrders = if (selectedStatus == "Semua") {
        orders.sortedBy { statusPriority[it.status] ?: 99 }
    } else {
        orders.filter { it.status.equals(selectedStatus, ignoreCase = true) }
    }

    Scaffold(
        bottomBar = { UserBottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()


        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2EADC9))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Pesanan Saya", fontSize = 24.sp, color = Color.White)
            }


            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(statusOptions) { status ->
                    val isSelected = status == selectedStatus
                    Button(
                        onClick = { selectedStatus = status },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF2EADC9) else Color.White,
                            contentColor = if (isSelected) Color.White else Color(0xFF2EADC9)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(text = status, fontSize = 14.sp)
                    }
                }
            }

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )


            if (error.isNotEmpty()) {
                Text("Error: $error", color = Color.Red)
            } else if (filteredOrders.isEmpty()) {
                Text("Tidak ada pesanan dengan status \"$selectedStatus\".")
            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(filteredOrders) { order ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()

                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {

                                Text("Order ID: ${order.id}", fontWeight = FontWeight.Bold)
                                Text("Status: ${order.status.replaceFirstChar { it.uppercase() }}")
                                Divider(
                                    color = Color.LightGray,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .fillMaxWidth()
                                )
                                Text("Alamat: ${order.address.detailAlamat}")
                                if (order.address.catatan.isNotEmpty()) {
                                    Text("Catatan: ${order.address.catatan}")
                                }
                                Divider(
                                    color = Color.LightGray,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .fillMaxWidth()
                                )
                                Text("Total: ${order.items.values.sum()} Produk : Rp ${order.total}")
                                Divider(
                                    color = Color.LightGray,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .fillMaxWidth()
                                )

                                when (order.status.lowercase()) {
                                    "belum bayar" -> {
                                        Button(
                                            onClick = {
                                                navController.navigate("bayar/${order.id}")
                                            },
                                            modifier = Modifier.align(Alignment.Start)
                                                .height(36.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF2EADC9),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(20.dp),
                                        ) {
                                            Text("Bayar")
                                        }
                                    }

                                    "selesai" -> {
                                        Button(
                                            onClick = {
                                                navController.navigate("review/$uid/${order.id}")
                                            },
                                            modifier = Modifier.align(Alignment.Start)
                                                .height(36.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF2EADC9),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(20.dp),
                                        ) {
                                            Text("Review")
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



