package com.project.ekanfinal.view.userView

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.project.ekanfinal.R
import com.project.ekanfinal.viewmodel.OrderViewModel

@Composable
fun PaymentMethod(
    navController: NavHostController,
    totalAmount: Float,
    orderID: String
)
 {
     val banks = listOf(
        "BCA" to "1234567890",
        "Mandiri" to "9876543210",
        "BNI" to "1122334455",
        "BRI" to "5566778899"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.iconback),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF2EADC9)
                )
            }
            Text(
                text = "Pilih Metode Pembayaran",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2EADC9)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val bankList = banks.toList()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(text = "Transfer Bank",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            bankList.chunked(2).forEach { rowBanks ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowBanks.forEach { (bankName, accountNumber) ->
                        Card(
                            shape = RoundedCornerShape(size = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = CardDefaults.outlinedCardBorder(),
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("payment_detail/$bankName/$accountNumber/${totalAmount}/$orderID")
                                },
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("$bankName", fontSize = 14.sp)
                            }
                        }
                    }

                    if (rowBanks.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

    }
}

@Composable
fun PaymentDetailScreen(
    navController: NavHostController,
    bankName: String,
    accountNumber: String,
    amount: Float,
    orderID: String,
    orderViewModel: OrderViewModel
) {

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    orderViewModel.setUser(Firebase.auth.currentUser?.uid ?: "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.iconback),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF2EADC9)
                )
            }
            Text(
                text = "Pembayaran",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2EADC9)
            )
        }

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Metode Pembayaran",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = bankName,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(text = "Nomor Rekening", fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp, top = 8.dp))

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$accountNumber",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(accountNumber))
                        Toast.makeText(context, "Nomor rekening disalin", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Salin nomor rekening"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(text = "Jumalah Transfer", fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp, top = 8.dp))

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rp ${"%,.0f".format(amount)}",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    color = Color.Red
                )

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(amount.toString()))
                        Toast.makeText(context, "Jumlah Transfer disalin", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Salin jumlah transfer"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("PaymentDetailScreen", "Updating order $orderID to status diproses")
                orderViewModel.updateOrderStatus(orderID, "diproses", {
                    Log.d("PaymentDetailScreen", "Update status sukses, navigasi ke pesanan")
                    navController.navigate("pesanan")
                }, { error ->
                    Log.e("PaymentDetailScreen", "Update status gagal", error)
                })
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2EADC9),
                contentColor = Color.White
            )
        ) {
            Text(text = "Konfirmasi Pembayaran")
        }

    }

}

