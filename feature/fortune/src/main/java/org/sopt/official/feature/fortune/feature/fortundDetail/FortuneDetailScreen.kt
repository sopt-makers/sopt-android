package org.sopt.official.feature.fortune.feature.fortundDetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data class FortuneDetail(val date: String)

@Composable
fun FortuneDetailRoute(
    date: String,
) {
    FortuneDetailScreen(
        date = date,
    )
}

@Composable
fun FortuneDetailScreen(
    date: String,
) {
    Text(text = "Fortune Detail Screen: $date")
}
