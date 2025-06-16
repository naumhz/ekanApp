package com.project.ekanfinal.view.userView

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.ekanfinal.R
import com.project.ekanfinal.model.data.AddressModel
import com.project.ekanfinal.viewmodel.AddressViewModel

@Composable
fun InputAlamatPage(navController: NavHostController,
                    viewModel: AddressViewModel, onSuccess: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
    var kota by remember { mutableStateOf("") }
    var provinsi by remember { mutableStateOf("") }
    var kodePos by remember { mutableStateOf("") }
    var detailAlamat by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }

    val context = LocalContext.current

    val isFormValid = nama.isNotBlank() && noHp.isNotBlank() && kota.isNotBlank()

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("listAlamat") }) {
                Icon(
                    painter = painterResource(id = R.drawable.iconback),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Tambah Alamat",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2EADC9)
            )
        }

        FormInputAlamat(
            nama = nama, onNamaChange = { nama = it },
            noHp = noHp, onNoHpChange = { noHp = it },
            kota = kota, onKotaChange = { kota = it },
            provinsi = provinsi, onProvinsiChange = { provinsi = it },
            kodePos = kodePos, onKodePosChange = { kodePos = it },
            detailAlamat = detailAlamat, onDetailAlamatChange = { detailAlamat = it },
            catatan = catatan, onCatatanChange = { catatan = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color(0xFF26A3D3))
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    val newAddress = AddressModel(
                        nama = nama,
                        noHp = noHp,
                        kota = kota,
                        provinsi = provinsi,
                        kodePos = kodePos,
                        detailAlamat = detailAlamat,
                        catatan = catatan
                    )
                    viewModel.addAddress(
                        newAddress,
                        onSuccess = {
                            onSuccess()
                        },
                        onError = { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Simpan", color = Color(0xFF26A3D3), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FormInputAlamat(
    nama: String,
    onNamaChange: (String) -> Unit,
    noHp: String,
    onNoHpChange: (String) -> Unit,
    kota: String,
    onKotaChange: (String) -> Unit,
    provinsi: String,
    onProvinsiChange: (String) -> Unit,
    kodePos: String,
    onKodePosChange: (String) -> Unit,
    detailAlamat: String,
    onDetailAlamatChange: (String) -> Unit,
    catatan: String,
    onCatatanChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        val shape = RoundedCornerShape(12.dp)
        val modifierBox = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)


        OutlinedTextField(
            value = nama,
            onValueChange = onNamaChange,
            label = { Text("Nama") },
            modifier = modifierBox.clip(shape)
        )

        OutlinedTextField(
            value = noHp,
            onValueChange = onNoHpChange,
            label = { Text("Nomor Telepon") },
            modifier = modifierBox.clip(shape)
        )

        OutlinedTextField(
            value = kota,
            onValueChange = onKotaChange,
            label = { Text("Kota") },
            modifier = modifierBox.clip(shape)
        )

        OutlinedTextField(
            value = provinsi,
            onValueChange = onProvinsiChange,
            label = { Text("Provinsi") },
            modifier = modifierBox.clip(shape)
        )

        OutlinedTextField(
            value = kodePos,
            onValueChange = onKodePosChange,
            label = { Text("Kode Pos") },
            modifier = modifierBox.clip(shape)
        )

        OutlinedTextField(
            value = detailAlamat,
            onValueChange = onDetailAlamatChange,
            label = { Text("Detail Alamat") },
            modifier = modifierBox.clip(shape),
            maxLines = 3
        )

        OutlinedTextField(
            value = catatan,
            onValueChange = onCatatanChange,
            label = { Text("Catatan") },
            modifier = modifierBox.clip(shape),
            maxLines = 2
        )
    }
}
