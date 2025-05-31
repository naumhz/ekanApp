package com.project.ekanfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.project.ekanfinal.ui.theme.EkanFinalTheme
import com.project.ekanfinal.view.RegisterPage
import com.project.ekanfinal.viewmodel.AddressViewModel
import com.project.ekanfinal.viewmodel.AuthViewModel
import com.project.ekanfinal.viewmodel.BannerViewModel
import com.project.ekanfinal.viewmodel.CartViewModel
import com.project.ekanfinal.viewmodel.OrderViewModel
import com.project.ekanfinal.viewmodel.ProductViewModel
import com.project.ekanfinal.viewmodel.ReviewViewModel
import com.project.ekanfinal.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>()
    private val productViewModel: ProductViewModel by viewModels<ProductViewModel>()
    private val bannerViewModel: BannerViewModel by viewModels<BannerViewModel>()
    private val cartViewModel: CartViewModel by viewModels<CartViewModel>()
    private val userViewModel: UserViewModel by viewModels<UserViewModel>()
    private val orderViewModel: OrderViewModel by viewModels<OrderViewModel>()
    private val addressViewModel: AddressViewModel by viewModels<AddressViewModel>()
    private val reviewViewModel: ReviewViewModel by viewModels<ReviewViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EkanFinalTheme {
                AppNavigation(
                    authViewModel = authViewModel,
                    productViewModel = productViewModel,
                    bannerViewModel = bannerViewModel,
                    cartViewModel = cartViewModel,
                    userViewModel = userViewModel,
                    orderViewModel = orderViewModel,
                    addressViewModel = addressViewModel,
                    reviewViewModel = reviewViewModel
                )
            }
        }
    }
}

