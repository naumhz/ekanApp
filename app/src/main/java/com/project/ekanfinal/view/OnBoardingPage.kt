package com.project.ekanfinal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.project.ekanfinal.R
import com.project.ekanfinal.ui.theme.Poppins
import com.project.ekanfinal.ui.theme.PrimaryColor
import com.project.ekanfinal.util.OnboardingPreference
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val imageRes: Int
)

@Composable
fun OnBoardingPage(navController: NavHostController){
    val onboardingPages = listOf(
        OnboardingPage(
            title = "Memberikan kemudahan untuk menjangkau ikan segar kualitas tinggi",
            imageRes = R.drawable.onboarding1
        ),
        OnboardingPage(
            title = "Melalui sumber tangkapan yang terjamin kualitasnya",
            imageRes = R.drawable.onboarding1
        ),
        OnboardingPage(
            title = "Dapatkan ikan berkualitas sekarang!",
            imageRes = R.drawable.onboarding1
        )
    )

    var currentPage by remember { mutableStateOf(0) }
    val isLastPage = currentPage == onboardingPages.lastIndex

    val context = LocalContext.current
    val onboardingPref = remember { OnboardingPreference(context) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = onboardingPages[currentPage].imageRes),
            contentDescription = "Onboarding Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(bottomStart = 72.dp))
                .background(Color.White)
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo E-KAN",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(56.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topEnd = 72.dp))
                .background(Color.White)
                .padding(36.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = onboardingPages[currentPage].title,
                fontFamily = Poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (isLastPage) {
                        coroutineScope.launch {
                            onboardingPref.setOnboardingCompleted(true)
                            navController.navigate("Register") {
                                popUpTo("Onboarding") { inclusive = true }
                            }
                        }
                    } else {
                        currentPage++
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = if (isLastPage) "Mulai" else "Lanjut",
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                onboardingPages.forEachIndexed { index, _ ->
                    val color = if (index <= currentPage) PrimaryColor else Color.Gray
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color)
                    )
                    if (index != onboardingPages.lastIndex) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}