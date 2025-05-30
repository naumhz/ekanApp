package com.project.ekanfinal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.ekanfinal.BottomNavigationBar
import com.project.ekanfinal.R
import com.project.ekanfinal.StatusStepIndicator
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.OrderViewModel
import com.project.ekanfinal.viewmodel.UserViewModel

data class Order(
    val id: Int,
    val products: List<OrderProduct>,
    val status: String
)

data class OrderProduct(
    val imageRes: Int,
    val name: String,
    val quantity: Int,
    val weight: String,
    val price: Int
)

val dummyOrders = listOf(
    Order(
        id = 1,
        status = "Belum Bayar",
        products = listOf(
            OrderProduct(R.drawable.ikantuna, "Ikan Tuna", 1, "1 kg", 150000),
            OrderProduct(R.drawable.ikancakalang, "Ikan Cakalang", 2, "500 gr", 100000)
        )),
    Order(
        id = 2,
        status = "Selesai",
        products = listOf(
            OrderProduct(R.drawable.lobster, "Lobster", 1, "1 kg", 250000)
        )
    )
)

@Composable
fun HistoryPage(navController: NavHostController) {
    var selectedStatus by remember { mutableStateOf("Semua") }
    val filteredOrders = if (selectedStatus == "Semua") dummyOrders else dummyOrders.filter { it.status == selectedStatus }

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(paddingValues)
        ) {
            Text(
                text = "Pesanan Saya",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2EADC9))
                    .padding(16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("Semua", "Belum Bayar", "Diproses", "Dikirim", "Selesai")) { status ->
                    Button(
                        onClick = { selectedStatus = status },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedStatus == status) Color(0xFF2EADC9) else Color.White,
                            contentColor = if (selectedStatus == status) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(text = status)
                    }
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                items(filteredOrders) { order ->
                    OrderCard(order)
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    val totalHarga = order.products.sumOf { it.price }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Status: ${order.status}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            order.products.forEach { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${product.quantity} x ${product.weight}", fontSize = 14.sp, color = Color.Gray)
                        Text(text = "Rp ${product.price}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: Rp $totalHarga",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.End)
            )

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { /* aksi */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EADC9))
                ) {
                    Text(text = if (order.status == "Selesai") "Review" else "Bayar")
                }
            }
        }
    }
}
