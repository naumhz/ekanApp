package com.project.ekanfinal.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.ekanfinal.viewmodel.AuthViewModel

@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavHostController, viewModel: AuthViewModel){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            color = Color(0xFF2EADC9),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val textFieldShape = RoundedCornerShape(16.dp)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            shape = textFieldShape
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = textFieldShape
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EADC9)),
            onClick = {
                isLoading = true
                viewModel.login( email, password){success, error ->
                    if (success){
                        isLoading = false
                        navController.navigate("home"){
                            popUpTo("onboarding"){inclusive = true}
                        }
                    }else{
                        isLoading = false
                    }
                }
                      },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp)
        ) {
            Text(text = "Login", fontSize = 16.sp)
        }


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                append("Belum memiliki akun?")
                pushStyle(SpanStyle(color = Color.Red))
                append(" Register")
                pop()
            },
            modifier = Modifier.clickable {
                navController.navigate("register")
            }
        )
    }

}
