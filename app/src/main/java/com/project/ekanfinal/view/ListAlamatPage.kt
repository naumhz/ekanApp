package com.project.ekanfinal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ekanfinal.model.data.AddressModel
import com.project.ekanfinal.viewmodel.AddressViewModel

@Composable
fun ListAlamatPage(navController: NavHostController, viewModel: AddressViewModel = viewModel()){

    val addresses by viewModel.addresses.observeAsState(emptyList())
    val error by viewModel.error.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAddresses()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                navController.navigate("profil")
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                navController.navigate("inputAlamat")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Alamat")
            }
        }

        if (addresses.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Belum ada alamat yang disimpan", color = Color.Gray)
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(addresses) { alamat ->
                    AddressItem(alamat)
                }
            }
        }

        error?.let {
            Text(text = "Error: $it", color = Color.Red)
        }
    }
}

@Composable
fun AddressItem(address: AddressModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White)
            .padding(12.dp)
    ) {
        Text(text = address.nama, color = Color.Black)
        Text(text = address.detailAlamat, color = Color.DarkGray)
        Text(text = "${address.kota}, ${address.provinsi} - ${address.kodePos}", color = Color.DarkGray)
        Text(text = "No HP: ${address.noHp}", color = Color.DarkGray)
        if (address.catatan.isNotBlank()) {
            Text(text = "Catatan: ${address.catatan}", color = Color.Gray, fontSize = 12.sp)
        }
    }
}