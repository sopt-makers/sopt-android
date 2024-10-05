package org.sopt.official.feature.fortune.feature.fortuneAmulet

import androidx.compose.ui.graphics.Color

data class FortuneAmuletState(
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
    val description: String = "",
    val imageColor: Color = Color.White,
    val imageUrl: String = "",
    val name: String = "",
    val nameSuffix: String = "이 왔솝",
)
