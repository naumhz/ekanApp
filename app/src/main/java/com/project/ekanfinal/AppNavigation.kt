package com.project.ekanfinal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.project.ekanfinal.view.CartPage
import com.project.ekanfinal.view.ChatPage
import com.project.ekanfinal.view.HistoryPage
import com.project.ekanfinal.view.HomePage
import com.project.ekanfinal.view.InputAlamatPage
import com.project.ekanfinal.view.ListAlamatPage
import com.project.ekanfinal.view.LoginPage
import com.project.ekanfinal.view.MakeOrderPage
import com.project.ekanfinal.view.OnBoardingPage
import com.project.ekanfinal.view.OrderSuccessPage
import com.project.ekanfinal.view.ProductDetailPage
import com.project.ekanfinal.view.ProductPage
import com.project.ekanfinal.view.ProfilePage
import com.project.ekanfinal.view.RegisterPage
import com.project.ekanfinal.view.SelectAddressPage
import com.project.ekanfinal.view.VoucherPage
import com.project.ekanfinal.viewmodel.AddressViewModel
import com.project.ekanfinal.viewmodel.AuthViewModel
import com.project.ekanfinal.viewmodel.BannerViewModel
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.OrderViewModel
import com.project.ekanfinal.viewmodel.ProductViewModel
import com.project.ekanfinal.viewmodel.UserViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    bannerViewModel: BannerViewModel,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    orderViewModel: OrderViewModel,
    addressViewModel: AddressViewModel
) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController


    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage =if (isLoggedIn) "home" else "onboarding"

    NavHost(navController, startDestination = firstPage) {

        composable("onboarding") {
            OnBoardingPage(navController = navController)
        }
        composable("register") {
            RegisterPage(navController = navController, viewModel = authViewModel)
        }
        composable("login") {
            LoginPage(navController = navController, viewModel = authViewModel)
        }
        composable("home") {
            HomePage(
                navController = navController,
                productViewModel = productViewModel,
                bannerViewModel = bannerViewModel,
                cartViewModel = cartViewModel,
                userViewModel = userViewModel
            )
        }

        composable("produk") {
            ProductPage(navController = navController, viewModel = productViewModel)
        }
        composable("keranjang") {
            CartPage(navController = navController)
        }
        composable("pesanan") {
            HistoryPage(navController = navController)
        }
        composable("profil") {
            ProfilePage(navController = navController)
        }
        
        composable("voucher") {
            VoucherPage(navController = navController, orderViewModel = orderViewModel)
        }

        composable("orderSuccess") {
            OrderSuccessPage(navController = navController)
        }

        composable("product-details/{productId}"){
            var productId = it.arguments?.getString("productId")
            ProductDetailPage(modifier = Modifier,productId?: "", navController, productViewModel, cartViewModel
                , userViewModel)
        }

        composable("order") {
            MakeOrderPage(navController = navController, viewModel = orderViewModel, addressViewModel = addressViewModel)
        }

        composable("chat?pesan={pesan}") { backStackEntry ->
            val pesan = backStackEntry.arguments?.getString("pesan") ?: ""
            ChatPage(pesan = pesan)
        }

        composable("inputAlamat") {
            InputAlamatPage(navController = navController, viewModel = addressViewModel, onSuccess = {
                navController.navigate("listAlamat")
            } )
        }

        composable("listAlamat") {
            ListAlamatPage(navController = navController, viewModel = addressViewModel)
        }

        composable("select_address") {
            SelectAddressPage(navController = navController, addressViewModel = addressViewModel)

        }
    }
}

object GlobalNavigation {
    lateinit var navController: NavHostController
}
