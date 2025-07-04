package com.project.ekanfinal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project.ekanfinal.R
import com.project.ekanfinal.util.OnboardingPreference
import com.project.ekanfinal.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

//MODIF DONE
@Composable
fun SplashScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()

    val context = LocalContext.current
    val onboardingPref = remember { OnboardingPreference(context) }

    val isCompleted by onboardingPref.isOnboardingCompleted.collectAsState(initial = false)
    val loginSuccess by authViewModel.loginSuccess.collectAsState()
    val userRole by authViewModel.userRole.collectAsState()

    LaunchedEffect(isCompleted, loginSuccess, userRole) {
        delay(2000)
        if (isCompleted) {
            if (loginSuccess == true) {
                if(userRole == "admin"){
                    navController.navigate("AdminHome"){
                        popUpTo("Splash") { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    navController.navigate("UserHome") {
                        popUpTo("Splash") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                navController.navigate("Login") {
                    popUpTo("Splash") { inclusive = true }
                    launchSingleTop = true
                }
            }
        } else {
            navController.navigate("OnBoarding") {
                popUpTo("Splash") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_splash),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )
        }
    }
}