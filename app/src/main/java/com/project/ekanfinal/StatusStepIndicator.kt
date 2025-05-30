package com.project.ekanfinal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusStepIndicator(currentStep: Int, allDisabled: Boolean = false) {
    val steps = listOf("Buat Pesanan", "Pembayaran", "Verifikasi")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, title ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.buatpp),
                        contentDescription = title,
                        tint = if (allDisabled) Color.Gray else if (index <= currentStep) Color(0xFF2EADC9) else Color.Gray
                    )
                }
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = if (allDisabled) Color.Gray else if (index <= currentStep) Color(0xFF2EADC9) else Color.Gray
                )
            }

            if (index < steps.lastIndex) {
                Spacer(
                    modifier = Modifier
                        .width(30.dp)
                        .height(2.dp)
                        .background(
                            when {
                                allDisabled -> Color.Gray
                                index < currentStep -> Color(0xFF2EADC9)
                                else -> Color.Gray
                            }
                        )
                )
            }
        }
    }
}
