package com.project.ekanfinal.view.userView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun OrderPage(modifier: Modifier = Modifier, navController: NavHostController){

    Column (
        modifier = modifier.fillMaxSize()
    ){
        Text(text = "Order Page")
    }
}
