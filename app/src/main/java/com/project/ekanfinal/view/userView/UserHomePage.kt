@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.ekanfinal.view.userView

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.ekanfinal.R
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.ui.theme.BackgroundColor
import com.project.ekanfinal.ui.theme.Poppins
import com.project.ekanfinal.ui.theme.PrimaryColor
import com.project.ekanfinal.util.formatRupiah
import com.project.ekanfinal.viewmodel.BannerViewModel
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.ProductViewModel
import com.project.ekanfinal.viewmodel.UserViewModel
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

//MODIF DONE, KURANG AKTIFIN YG DI KOMEN KRN CHAT BLUM DIBIKIN
@Composable
fun UserHomePage(navController: NavHostController) {
    val productViewModel: ProductViewModel = viewModel()
    val bannerViewModel: BannerViewModel = viewModel()
//    BELUM BIKIN FUNGSI CHAT
//    val chatRepository = remember { ChatRepository() }
//    val chatViewModel : ChatViewModel = viewModel(factory = ChatViewModelFactory(chatRepository))

//    DIPAKE DI BAWAH
//    val cartViewModel: CartViewModel = viewModel()
//    val userViewModel: UserViewModel = viewModel()

    val products by productViewModel.productList.collectAsState()
    val banners by bannerViewModel.banners.collectAsState()
    val promoProducts = products.filter { it.diskon > 0 }
    val adminUid = remember { mutableStateOf<String?>(null) }

//    AKTIFIN KALO UDAH ADA FUNGSI ADMIN
//    LaunchedEffect(Unit) {
//        chatViewModel.getAdminUID { uid ->
//            adminUid.value = uid
//        }
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_logo),
                            contentDescription = "Logo E-Kan",
                            modifier = Modifier.size(120.dp)
                        )

                        IconButton(onClick = {
                            adminUid.value?.let { uid ->
                                navController.navigate("ChatScreen/$uid")
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.HeadsetMic,
                                tint = PrimaryColor,
                                contentDescription = "Chat dengan Admin",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        },
        bottomBar = { UserBottomBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(bottom = innerPadding.calculateBottomPadding(), top = innerPadding.calculateTopPadding())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (banners.isNotEmpty()) {
                    val pagerState = rememberPagerState(0) { banners.size }
                    Column(modifier = Modifier.padding(top = 0.dp)) {
                        HorizontalPager(state = pagerState, pageSpacing = 24.dp) {
                            AsyncImage(
                                model = banners.getOrNull(it),
                                contentDescription = "Banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(18.dp))
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        DotsIndicator(
                            dotCount = banners.size,
                            type = ShiftIndicatorType(
                                DotGraphic(color = PrimaryColor, size = 6.dp)
                            ),
                            pagerState = pagerState
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (products.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = PrimaryColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Memuat produk...",
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    Text(
                        text = "Popular",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    val topReviewedProducts = products
                        .sortedByDescending { it.reviewCount }
                        .take(6)

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(topReviewedProducts.size) { index ->
                            ProductCard(
                                product = products[index],
                                navController = navController
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Hot Promo",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
            }

            items(promoProducts.size) { index ->
                ProductCardPromo(
                    product = promoProducts[index],
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun ProductCard(product: ProductModel, navController: NavHostController) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clickable {
                navController.navigate("ProductDetail/${product.id}")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.nama,
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.nama,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                fontFamily = Poppins,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Text(
                text = formatRupiah(product.hargaDiskon) + "/kg",
                color = Color.Gray,
                fontSize = 12.sp,
                fontFamily = Poppins,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProductCardPromo(product: ProductModel, navController: NavHostController) {
    val cartViewModel: CartViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.user.value
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        cartViewModel.toastMessage.collect { message ->
            if (message.contains("berhasil", ignoreCase = true)) {
                showDialog = true
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                navController.navigate("ProductDetail/${product.id}")
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.nama,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nama,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    fontFamily = Poppins
                )

                Text(
                    text = formatRupiah(product.harga),
                    fontSize = 12.sp,
                    color = Color.Red,
                    textDecoration = TextDecoration.LineThrough,
                    fontFamily = Poppins
                )

                Text(
                    text = formatRupiah(product.hargaDiskon) + "/kg",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    color = Color.Black
                )
            }

            IconButton(
                onClick = {
                    user?.uid?.let {
                        showDialog = true
                        cartViewModel.addToCart(product.id)
                    }
                },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(25.dp)
                    .background(PrimaryColor, shape = RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Add to Cart",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color.White,
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Berhasil menambahkan produk ke keranjang.",
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Ok", fontFamily = Poppins, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            showDialog = false
                            navController.navigate("UserCart")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Lihat Keranjang", fontFamily = Poppins, color = Color.White)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

