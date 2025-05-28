package com.project.ekanfinal

import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object HomePage : BottomNavItem("home", R.drawable.navhome, "Home")
    object ProductPage : BottomNavItem("produk", R.drawable.navproduk, "Produk")
    object CartPage : BottomNavItem("keranjang", R.drawable.navkeranjang, "Cart")
    object HistoryPage : BottomNavItem("pesanan", R.drawable.navriwayat, "Pesanan")
    object ProfilePage : BottomNavItem("profil", R.drawable.navprofil, "Profil")
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.HomePage,
        BottomNavItem.ProductPage,
        BottomNavItem.CartPage,
        BottomNavItem.HistoryPage,
        BottomNavItem.ProfilePage
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) Color(0xFF2EADC9) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = if (isSelected) Color(0xFF2EADC9) else Color.Gray
                    )
                },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}