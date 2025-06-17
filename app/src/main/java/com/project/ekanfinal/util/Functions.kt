package com.project.ekanfinal.util

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(number: Int?): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(number)
}