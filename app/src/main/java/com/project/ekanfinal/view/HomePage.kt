package com.project.ekanfinal.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.ekanfinal.BottomNavigationBar
import com.project.ekanfinal.GlobalNavigation
import com.project.ekanfinal.R
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.viewmodel.BannerViewModel
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.ProductViewModel
import com.project.ekanfinal.viewmodel.UserViewModel
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    productViewModel: ProductViewModel,
    bannerViewModel: BannerViewModel,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel
) {
    val products by productViewModel.productList.collectAsState()
    val banners by bannerViewModel.banners.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.textekan),
                        contentDescription = "Text",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            item {
                val pagerState = rememberPagerState(0) { banners.size }
                Column {
                    HorizontalPager(state = pagerState, pageSpacing = 24.dp) {
                        AsyncImage(
                            model = banners.getOrNull(it),
                            contentDescription = "Banner",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    DotsIndicator(
                        dotCount = banners.size,
                        type = ShiftIndicatorType(
                            DotGraphic(
                                color = MaterialTheme.colorScheme.primary,
                                size = 6.dp
                            )
                        ),
                        pagerState = pagerState
                    )
                }
            }

            item {
                Text(
                    "Popular",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products.size) { product ->
                        ProductCard(product = products[product])
                    }
                }
            }

            item {
                Text(
                    "Hot Promo",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            items(products.size) { product ->
                ProductCardPromo(product = products[product], cartViewModel, userViewModel)
            }
        }
    }
}


@Composable
fun ProductCard(product: ProductModel) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.nama,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = "Rp ${product.harga}/kg",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProductCardPromo(product: ProductModel, cartViewModel: CartViewModel,
                     userViewModel: UserViewModel
) {
    val user = userViewModel.user.value

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        cartViewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(8.dp),
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(product.nama, fontWeight = FontWeight.Normal)
                Text("Rp ${product.harga}/kg", color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.homecart),
                    contentDescription = "Cart",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            user?.uid?.let { cartViewModel.addToCart(product.id) }
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.homeinfo),
                    contentDescription = "Info",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            GlobalNavigation.navController.navigate("product-details/"+product.id)

                        }
                )
            }
        }
    }

}

