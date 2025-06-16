package com.project.ekanfinal.view.userView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.ekanfinal.BottomNavigationBar
import com.project.ekanfinal.GlobalNavigation
import com.project.ekanfinal.R
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.viewmodel.ProductViewModel

@Composable
fun ProductPage(modifier: Modifier = Modifier, navController: NavHostController,
                viewModel: ProductViewModel
) {

    val products by viewModel.productList.collectAsState()
    val searchText = remember { mutableStateOf("") }

    val filteredProducts = products.filter {
        it.nama.contains(searchText.value, ignoreCase = true)
    }


    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2EADC9))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Katalog",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Discover",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 26.dp)
            )

            TextField(
                value = searchText.value,
                onValueChange = { searchText.value = it },
                placeholder = { Text("Search...") },
                modifier = Modifier
                    .padding(horizontal = 26.dp, vertical = 8.dp)
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
                        painter = painterResource(id = R.drawable.serachicon),
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Popular",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 26.dp)
                )

                Button(
                    onClick = { },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .height(36.dp)
                        .width(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EADC9))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription = "Filter",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                items(filteredProducts.chunked(2)) { rowItems ->
                    Row {
                        rowItems.forEach {
                            ProductItemView(product = it, modifier = Modifier.weight(1f))
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

@Composable
fun ProductItemView(modifier: Modifier = Modifier, product: ProductModel){
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable{
                GlobalNavigation.navController.navigate("product-details/"+product.id)
            }
        ,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column (){
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.nama,
                modifier = Modifier.height(120.dp)
                    .fillMaxWidth()
            )
            Text(text = product.nama, fontWeight = FontWeight.Bold,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )

            Text(text = "Rp ${product.harga}/kg",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
