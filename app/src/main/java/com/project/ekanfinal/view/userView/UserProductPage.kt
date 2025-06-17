@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.ekanfinal.view.userView

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SettingsInputComponent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.ekanfinal.GlobalNavigation
import com.project.ekanfinal.R
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.ui.theme.BackgroundColor
import com.project.ekanfinal.ui.theme.Poppins
import com.project.ekanfinal.ui.theme.PrimaryColor
import com.project.ekanfinal.util.formatRupiah
import com.project.ekanfinal.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import kotlin.compareTo
import kotlin.text.chunked
import kotlin.text.forEach

//MODIF DONE KURANG MIGRATE KATEGORI KE DATABASE, MASIH PAKE VARIABEL LOKAL
@Composable
fun UserProductPage(navController: NavHostController) {
    val viewModel : ProductViewModel = viewModel()
    val products by viewModel.productList.collectAsState()

    val searchText = remember { mutableStateOf("") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val selectedCategory = remember { mutableStateOf<String?>(null) }
    val priceMin = remember { mutableStateOf("") }
    val priceMax = remember { mutableStateOf("") }

    val filteredProducts by remember(
        products,
        searchText.value,
        selectedCategory.value,
        priceMin.value,
        priceMax.value
    ) {
        derivedStateOf {
            val min = priceMin.value.toIntOrNull() ?: Int.MIN_VALUE
            val max = priceMax.value.toIntOrNull() ?: Int.MAX_VALUE

            products.filter { prod ->
                prod.nama.contains(searchText.value, ignoreCase = true) &&
                        (selectedCategory.value == null || prod.kategori == selectedCategory.value) &&
                        prod.hargaDiskon in min..max
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                FilterSidebar(
                    selectedCategory = selectedCategory,
                    priceMin = priceMin,
                    priceMax = priceMax,
                    onApply = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Katalog",
                            fontSize = 24.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryColor
                    )
                )
            },
            bottomBar = { UserBottomBar(navController) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(bottom = innerPadding.calculateBottomPadding(), top = innerPadding.calculateTopPadding())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Discover",
                        fontSize = 21.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    TextField(
                        value = searchText.value,
                        onValueChange = { searchText.value = it },
                        placeholder = {
                            Text(
                                text = "Cari...",
                                fontSize = 16.sp,
                                fontFamily = Poppins,
                                color = Color.White
                            )
                        },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0x802EADC9),
                            unfocusedContainerColor = Color(0x802EADC9),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 8.dp)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Popular",
                            fontSize = 21.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Button(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .height(36.dp)
                                .width(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.filter),
                                contentDescription = "Filter",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredProducts.chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            rowItems.forEach { prod ->
                                ProductCard(
                                    product = prod,
                                    modifier = Modifier.weight(1f),
                                    navController = navController
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterSidebar(
    selectedCategory: MutableState<String?>,
    priceMin: MutableState<String>,
    priceMax: MutableState<String>,
    onApply: () -> Unit
) {
    val categories = listOf("Ikan Air Tawar", "Ikan Air Laut", "Udang & Cumi", "Kerang & Kepiting", "Olahan Ikan")

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Text(
            text = "Filter",
            fontSize = 21.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Kategori", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, fontFamily = Poppins)

        Spacer(modifier = Modifier.height(8.dp))

        categories.forEach { cat ->
            val isSelected = selectedCategory.value == cat
            Button(
                onClick = {
                    selectedCategory.value = if (isSelected) null else cat
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) PrimaryColor else Color.Transparent,
                    contentColor = if (isSelected) Color.White else Color.Black
                ),
                border = if (!isSelected) BorderStroke(1.dp, Color.Gray) else null,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(cat, fontFamily = Poppins)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Harga (Rp)", fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = Poppins)

        Spacer(modifier = Modifier.height(16.dp))
        val borderColor = PrimaryColor

        TextField(
            value = priceMin.value,
            onValueChange = {
                if (it.all { ch -> ch.isDigit() } || it.isEmpty()) {
                    priceMin.value = it
                }
            },
            label = { Text("Min", fontFamily = Poppins) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = borderColor,
                unfocusedIndicatorColor = borderColor,
                disabledIndicatorColor = borderColor
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = priceMax.value,
            onValueChange = {
                if (it.all { ch -> ch.isDigit() } || it.isEmpty()) {
                    priceMax.value = it
                }
            },
            label = { Text("Max", fontFamily = Poppins) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = borderColor,
                unfocusedIndicatorColor = borderColor,
                disabledIndicatorColor = borderColor
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = onApply,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text("Terapkan Filter", fontFamily = Poppins, color = Color.White)
        }
    }
}

@Composable
fun ProductCard(product: ProductModel, modifier: Modifier = Modifier, navController: NavHostController) {
    Card(
        modifier = modifier
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

            val hargaDisplay = if (product.diskon > 0) product.hargaDiskon else product.harga
            Text(
                text = formatRupiah(hargaDisplay) + "/kg",
                color = Color.Gray,
                fontSize = 12.sp,
                fontFamily = Poppins,
                textAlign = TextAlign.Center
            )
        }
    }
}
