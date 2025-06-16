package com.project.ekanfinal.view.userView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ekanfinal.viewmodel.AddressViewModel

@Composable
fun SelectAddressPage(
    navController: NavHostController,
    addressViewModel: AddressViewModel = viewModel()
) {

    val addresses by addressViewModel.addresses.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        addressViewModel.fetchAddresses()
    }

    Column (
        modifier = Modifier.padding(16.dp)
    ){
        LazyColumn {
            items(addresses) { address ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            addressViewModel.selectAddress(address)
                            navController.popBackStack()
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = address.nama, fontWeight = FontWeight.Bold)
                        Text(text = address.noHp)
                        Text(text = "${address.detailAlamat}, ${address.kota}, ${address.provinsi} ${address.kodePos}")
                    }
                }
            }
        }

    }

}
