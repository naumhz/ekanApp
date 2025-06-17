package com.project.ekanfinal.view.userView

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.UserViewModel
import java.net.URLEncoder

@Composable
fun UserCartPage(modifier: Modifier = Modifier, navController: NavHostController,
             viewModel: CartViewModel = viewModel(), userViewModel : UserViewModel = viewModel()
) {

    val user by userViewModel.user
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        bottomBar = { UserBottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2EADC9))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Keranjang", fontSize = 24.sp, color = Color.White)
                }



                Spacer(modifier = Modifier.height(4.dp))

                if(user!=null) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        items(user!!.cartItems.toList(), key = { it.first }) { (productId, qty) ->
                            CartItemView(
                                productId = productId,
                                qty = qty,
                                cartViewModel = viewModel
                            )
                        }

                    }
                }


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Button(
                            onClick = {
                                val cartSummary = user?.cartItems?.entries?.joinToString("\n") { (id, qty) ->
                                    val product = viewModel.product.value
                                    "- $product x$qty"
                                } ?: "Keranjang kosong."

                                val encodedMessage = URLEncoder.encode(cartSummary, "UTF-8")
                                navController.navigate("chat?pesan=$encodedMessage")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EADC9))
                        ) {
                            Text("Cek Stok", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                navController.navigate("order")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EADC9))

                        ) {
                            Text("Buat Pesanan", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CartItemView(
    modifier: Modifier = Modifier,
    productId: String,
    qty: Long,
    cartViewModel: CartViewModel
) {
    val products by cartViewModel.product

    LaunchedEffect(key1 = productId) {
        cartViewModel.loadProduct(productId)
    }

    val product = products[productId]

    if (product != null) {
        CartItemCard(
            product = product,
            qty = qty,
            onIncrease = {
                cartViewModel.addToCart(productId)
            },
            onDecrease = {
                cartViewModel.RemoveFromCart(productId)
            },
            onRemove = {
                cartViewModel.RemoveFromCart(productId, removeAll = true)
            }

        )
    }
}


@Composable
fun CartItemCard(
    product: ProductModel,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    qty: Long
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
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
                Text(text = "Rp ${product.harga}", fontSize = 16.sp, color = Color.Gray)
                Text(text = "Kg", fontSize = 14.sp, color = Color.Gray)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                IconButton(onClick = onDecrease) {
                    Text(text = "-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Text(text =  "$qty" , fontSize = 18.sp, color = Color.Black)
                IconButton(onClick = onIncrease) {
                    Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            IconButton(onClick = onRemove) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Hapus", tint = Color.Black)
            }
        }
    }
}