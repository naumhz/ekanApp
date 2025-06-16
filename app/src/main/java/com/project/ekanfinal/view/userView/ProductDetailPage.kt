package com.project.ekanfinal.view.userView

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.ekanfinal.R
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.ProductViewModel
import com.project.ekanfinal.viewmodel.UserViewModel

@Composable
fun ProductDetailPage(modifier: Modifier = Modifier,productID:String, navController: NavHostController,
                      viewModel: ProductViewModel , cartViewModel : CartViewModel,
                      userViewModel: UserViewModel
){
    val product = viewModel.selectedProduct.collectAsState()
    val user = userViewModel.user.value

    LaunchedEffect(productID) {
        viewModel.getProductById(productID)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        cartViewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.iconback),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Detail Produk",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF2EADC9)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.onboarding1),
                contentDescription = "Gambar Produk",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = product.value?.nama?: "Nama Produk",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp. ${product.value?.harga ?: "Harga Produk"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, Color(0xFF9D9EA3), RoundedCornerShape(8.dp)) // Outline abu-abu
                        .padding(12.dp)
                        .clickable {
                            navController.navigate("reviewList/$productID")
                        }
                ) {
                    Text(
                        text = product.value?.rating?.toString() ?: "Rating Produk",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.starreview),
                        contentDescription = "Star",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lihat Penilaian Produk",
                        fontSize = 14.sp,
                        color = Color(0xFF2EADC9),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.forward),
                        contentDescription = "Arrow",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Deskripsi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product.value?.deskripsi ?: "Deskripsi Produk",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val labelColor = Color.Gray
                val labelFontSize = 12.sp
                val valueFontSize = 14.sp
                val valueFontWeight = FontWeight.Bold

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("Origin", color = labelColor, fontSize = labelFontSize)
                    Text(product.value?.origin ?: "-", fontSize = valueFontSize, fontWeight = valueFontWeight)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("Cara Penyajian", color = labelColor, fontSize = labelFontSize, textAlign = TextAlign.Center)
                    Text(product.value?.penyajian ?: "-", fontSize = valueFontSize, fontWeight = valueFontWeight, textAlign = TextAlign.Center)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("Kondisi Seafood", color = labelColor, fontSize = labelFontSize, textAlign = TextAlign.Center)
                    Text(product.value?.kondisi ?: "-", fontSize = valueFontSize, fontWeight = valueFontWeight, textAlign = TextAlign.Center)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("Jenis Potongan", color = labelColor, fontSize = labelFontSize, textAlign = TextAlign.Center)
                    Text(product.value?.potongan ?: "-", fontSize = valueFontSize, fontWeight = valueFontWeight, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .shadow(20.dp, shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                .background(Color.White, shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var showDialog by remember { mutableStateOf(false) }

                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chatadmin),
                        contentDescription = "Chat",
                        modifier = Modifier.size(28.dp),
                        tint = Color(0xFF2EADC9)
                    )
                }

                Button(
                    onClick = {
                        user?.uid?.let { cartViewModel.addToCart(productID) }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EADC9)),
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(48.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.navkeranjang),
                            contentDescription = "Keranjang",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tambah Keranjang",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
