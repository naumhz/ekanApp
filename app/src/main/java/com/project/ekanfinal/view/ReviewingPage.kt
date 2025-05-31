package com.project.ekanfinal.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.project.ekanfinal.viewmodel.ReviewViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.ekanfinal.R

@Composable
fun ReviewingPage(
    navController: NavController,
    uid: String,
    orderId: String,
    viewModel: ReviewViewModel = viewModel()
) {
    val products by viewModel.products.collectAsState()
    var orderLoaded by remember { mutableStateOf(false) }

    val ratings = remember { mutableStateOf(mutableMapOf<String, Float>()) }
    val comments = remember { mutableStateOf(mutableMapOf<String, String>()) }

    LaunchedEffect(orderId) {
        if (!orderLoaded) {
            viewModel.loadProductsForOrder(uid, orderId)
            orderLoaded = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Review",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2EADC9)
            )
        }


    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
    )

    LazyColumn(modifier = Modifier
        .weight(1f)
        .padding(bottom = 16.dp)
    ) {
        items(products) { product ->
            val rating = ratings.value[product.id] ?: 0f
            val comment = comments.value[product.id] ?: ""

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = " ",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                        Text(text = product.nama, fontSize = 18.sp, color = Color.Black)
                        Text(text = "1 Kg", fontSize = 14.sp, color = Color.Gray)
                    }

                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Rp ${product.harga}", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ){

                RatingBar(rating = rating) { selected ->
                    ratings.value = ratings.value.toMutableMap().apply {
                        this[product.id] = selected
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = comment,
                        onValueChange = {
                            val updated = comments.value.toMutableMap()
                            updated[product.id] = it
                            comments.value = updated
                        },
                        label = { Text("Komentar") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
             }
        }

    Button(onClick = {
        if (ratings.value.values.any { it <= 0f }) {
            Toast.makeText(navController.context, "Semua produk harus diberi rating", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.submitAllReview(
                ratings = ratings.value,
                comments = comments.value,
                products = products,
                orderId = orderId,
                onSuccess = {
                    Toast.makeText(navController.context, "Review berhasil dikirim", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onError = {
                    Toast.makeText(navController.context, "Gagal mengirim review", Toast.LENGTH_SHORT).show()
                }
            )
        }
    },
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(16.dp)
            .height(48.dp)
            .width(200.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2EADC9),
            contentColor = Color.White
        )
    ) {
        Text("Review")
    }
    }
}



@Composable
fun RatingBar(rating: Float, onRatingChanged: (Float) -> Unit) {
    Row {
        for (i in 1..5) {
            val icon = if (i <= rating) Icons.Default.Star else Icons.Outlined.Star
            val tint = if (i <= rating) Color.Yellow else Color.Gray

            Icon(
                imageVector = icon,
                contentDescription = "Rating Star $i",
                tint = tint,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChanged(i.toFloat()) }
            )
        }
    }
}




