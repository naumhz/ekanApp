package com.project.ekanfinal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.project.ekanfinal.view.PayPage
import com.project.ekanfinal.view.PaymentDetailScreen
import com.project.ekanfinal.view.PaymentMethod
import com.project.ekanfinal.view.ProductDetailPage
import com.project.ekanfinal.view.ProductPage
import com.project.ekanfinal.view.ProfilePage
import com.project.ekanfinal.view.RegisterPage
import com.project.ekanfinal.view.ReviewPage
import com.project.ekanfinal.view.ReviewingPage
import com.project.ekanfinal.view.SelectAddressPage
import com.project.ekanfinal.view.VoucherPage
import com.project.ekanfinal.viewmodel.AddressViewModel
import com.project.ekanfinal.viewmodel.AuthViewModel
import com.project.ekanfinal.viewmodel.BannerViewModel
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.OrderViewModel
import com.project.ekanfinal.viewmodel.ProductViewModel
import com.project.ekanfinal.viewmodel.ReviewViewModel
import com.project.ekanfinal.viewmodel.UserViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    bannerViewModel: BannerViewModel,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    orderViewModel: OrderViewModel,
    addressViewModel: AddressViewModel,
    reviewViewModel: ReviewViewModel
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
            HistoryPage(navController = navController, uid = Firebase.auth.currentUser?.uid ?: "")
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

        composable(
            route = "bayar/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            PayPage(navController = navController, orderId = orderId)
        }

        composable(
            route = "metode_pembayaran/{totalAmount}/{orderID}",
            arguments = listOf(
                navArgument("totalAmount") { type = NavType.FloatType },
                navArgument("orderID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val totalAmount = backStackEntry.arguments?.getFloat("totalAmount") ?: 0f
            val orderID = backStackEntry.arguments?.getString("orderID") ?: ""
            PaymentMethod(navController = navController, totalAmount = totalAmount, orderID = orderID)
        }


        composable(
            route = "payment_detail/{bankName}/{accountNumber}/{amount}/{orderID}",
            arguments = listOf(
                navArgument("bankName") { type = NavType.StringType },
                navArgument("accountNumber") { type = NavType.StringType },
                navArgument("amount") { type = NavType.FloatType },
                navArgument("orderID") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bankName = backStackEntry.arguments?.getString("bankName") ?: ""
            val accountNumber = backStackEntry.arguments?.getString("accountNumber") ?: ""
            val amount = backStackEntry.arguments?.getFloat("amount") ?: 0f
            val orderID = backStackEntry.arguments?.getString("orderID") ?: ""

            PaymentDetailScreen(
                navController = navController,
                bankName = bankName,
                accountNumber = accountNumber,
                amount = amount,
                orderID = orderID,
                orderViewModel = viewModel()
            )
        }

        composable("review/{uid}/{orderId}") { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            ReviewingPage(navController, uid, orderId)
        }

        composable("reviewList/{productID}") {
            val productID = it.arguments?.getString("productID") ?: ""
            ReviewPage(navController = navController, productID)
        }
    }

    }


object GlobalNavigation {
    lateinit var navController: NavHostController
}
