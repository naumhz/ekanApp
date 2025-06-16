package com.project.ekanfinal.view.userView

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.ekanfinal.R
import com.project.ekanfinal.viewmodel.ReviewViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import com.project.ekanfinal.model.data.ReviewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun ReviewPage(navController: NavController,productID: String, viewModel: ReviewViewModel = viewModel()){

    var selectedRating by remember { mutableStateOf("Semua") }
    val ratingOptions = listOf("Semua", "5", "4", "3", "2", "1")

    val reviews by viewModel.reviews.observeAsState()

    LaunchedEffect(productID) {
        viewModel.loadReviews(productID)

    }

    val filteredReviews = if (selectedRating == "Semua") {
        reviews ?: emptyList()
    } else {
        val selectedRatingInt = selectedRating.toIntOrNull() ?: 0
        reviews?.filter { it.rating.toInt() == selectedRatingInt } ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.iconback),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF2EADC9)
                )
            }
            Text(
                text = "Penilaian Produk",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color(0xFF2EADC9)
            )
        }

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(ratingOptions) { rating ->
                val isSelected = rating == selectedRating

                Button(
                    onClick = { selectedRating = rating },
                    colors = if (isSelected) {
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2EADC9),
                            contentColor = Color.White
                        )
                    } else {
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF2EADC9)
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(36.dp),
                    border = if (isSelected) null else BorderStroke(1.dp, Color(0xFF2EADC9))
                ) {
                    if (rating == "Semua") {
                        Text(text = rating, fontSize = 14.sp)
                    } else {
                        Text(text = rating, fontSize = 14.sp)
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = if (isSelected) Color.White else Color(0xFF2EADC9),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth())

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ){
            items(filteredReviews) { review ->
                Column(modifier = Modifier.padding(12.dp)) {
                    ReviewItem(review = review)
                    Divider(color = Color.LightGray, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: ReviewModel) {
    val dateFormatted = remember(review.timestamp) {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(review.timestamp?.toDate() ?: Date())
        } catch (e: Exception) {
            "-"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = review.userName ?: "Anonim",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                repeat(5) { index ->
                    if (index < review.rating.toInt()) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }

            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = dateFormatted,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = review.comment ?: "Tidak ada komentar",
            fontSize = 14.sp
        )
    }
}
