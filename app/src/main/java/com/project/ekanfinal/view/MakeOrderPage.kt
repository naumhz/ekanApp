package com.project.ekanfinal.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.project.ekanfinal.R
import com.project.ekanfinal.StatusStepIndicator
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.OrderViewModel
import com.project.ekanfinal.viewmodel.UserViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.project.ekanfinal.model.data.AddressModel
import com.project.ekanfinal.viewmodel.AddressViewModel

@Composable
fun MakeOrderPage(modifier: Modifier = Modifier, navController: NavHostController,
                 viewModel: OrderViewModel = viewModel(),
                  addressViewModel: AddressViewModel = viewModel()
                 ){
    val scrollState = rememberScrollState()
    val products = viewModel.productList
    val total = viewModel.total.value
    val ongkir = viewModel.ongkir.value
    val diskonMember = viewModel.diskonMember.value
    val diskonOngkir = viewModel.diskonOngkir.value
    val totalFinal = viewModel.totalFinal.value
    val totalSetelahDiskon = viewModel.totalSetelahDiskon.value

    val selectedAddress by addressViewModel.selectedAddress.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                androidx.compose.material3.Icon(
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
        StatusStepIndicator(currentStep = 0, allDisabled = true)
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

            Column {
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
                ) {
                    Text(
                        text = "Alamat Pengiriman",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                    )

                    Text(
                        text = "Ganti",
                        modifier = Modifier.padding(end = 16.dp)
                            .clickable {
                                navController.navigate("select_address")
                            }
                        ,
                        color = Color(0xFF2EADC9)
                    )
                }
                Column(modifier = Modifier.padding(16.dp)) {

                    Spacer(modifier = Modifier.height(8.dp))
                    if (selectedAddress != null) {
                        Text(text = selectedAddress!!.nama)
                        Text(text = selectedAddress!!.noHp)
                        Text(
                            text = "${selectedAddress!!.detailAlamat}, " +
                                    "${selectedAddress!!.kota}, " +
                                    "${selectedAddress!!.provinsi} " +
                                    "${selectedAddress!!.kodePos}"
                        )
                    } else {
                        Text(text = "Pilih alamat pengiriman")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ){
            Column {

                Text(
                    text = "Keranjang",
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

                products.forEach { product ->
                    ProductItem(product = product, viewModel = viewModel)
                }

                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Row(Modifier.fillMaxWidth()) {
                        Text("Total Harga", modifier = Modifier.weight(1f))
                        Text("Rp $total")
                    }
                    Row(Modifier.fillMaxWidth()) {
                        Text("Ongkir", modifier = Modifier.weight(1f))
                        Text("Rp $ongkir")
                    }
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            text = "Total Akhir",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Rp $totalFinal",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }
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

            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Voucher",
                        modifier = Modifier
                            .weight(1f)
                    )

                    Text(
                        text = "Pakai Voucher >",
                        color = Color(0xFF2EADC9)
                    )
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Diskon Member",
                            modifier = Modifier.weight(1f)
                        )
                        Text(text = "Rp $diskonMember")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Diskon Ongkos Kirim",
                            modifier = Modifier.weight(1f)
                        )
                        Text(text = "Rp $diskonOngkir")
                    }
                }

            }
        }
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

                    Column {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Rincian Pembayaran",
                                modifier = Modifier
                                    .weight(1f)
                            )

                        }
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Pembelian",
                                    modifier = Modifier.weight(1f)
                                )
                                Text(text = "Rp $totalFinal")

                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Biaya Pengiriman",
                                    modifier = Modifier.weight(1f)
                                )
                                Text(text = "Rp $ongkir")
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Diskon Member",
                                    modifier = Modifier.weight(1f)
                                )
                                Text(text = "Rp $diskonMember")
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Diskon Ongkos Kirim",
                                    modifier = Modifier.weight(1f)
                                )
                                Text(text = "Rp $diskonOngkir")
                            }
                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tagihan Saat Ini",
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = "Rp ${totalSetelahDiskon}",
                                )

                            }

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
            Button(
                onClick = {
                    navController.navigate("orderSuccess")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(
                        0xFF2EADC9
                    )
                )
            ) {
                Text("Bayar", color = Color.White)
            }
        }

        }
    }


@Composable
fun ProductItem(product: ProductModel,
                viewModel: OrderViewModel = viewModel()
) {
    val qty = viewModel.user.value.cartItems[product.id] ?: 0

    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ){

        AsyncImage(
            model = product.imageUrl,
            contentDescription = " ",
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = product.nama)
            Text(text = "1 kg")
        }

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(end = 16.dp, top = 16.dp)
        ){
            Text(text = "$qty x")
            Text(text = "Rp ${product.harga.toInt() * qty}")
        }
    }
}
