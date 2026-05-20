package org.sopt.official.feature.sopletter.main.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

enum class SopletterMemoRotationType(
    val degree: Float,
) {
    LEFT(-10f),
    CENTER(0f),
    RIGHT(10f),
}

enum class SopletterMemoColor(
    val color: Color,
) {
    BLUE(Color(0xFFC8E1FF)),
    MINT(Color(0xFFCCFFEC)),
    PINK(Color(0xFFFFD1D3)),
    YELLOW(Color(0xFFFFF4D4)),
}

data class SopletterMemoUiModel(
    val id: Long,
    val message: String,
    @param:DrawableRes val shapeImageRes: Int,
    val rotation: SopletterMemoRotationType,
    val memoColor: SopletterMemoColor,
)
