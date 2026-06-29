package org.sopt.official.feature.sopletter.main.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.domain.sopletter.model.SopletterShapeType
import org.sopt.official.sopletter.R

@DrawableRes
internal fun SopletterShapeType.imageRes(): Int = when (this) {
    SopletterShapeType.SMOOTH -> R.drawable.ic_sopletter_memo_smooth
    SopletterShapeType.SHARP -> R.drawable.ic_sopletter_memo_sharp
    SopletterShapeType.POINT -> R.drawable.ic_sopletter_memo_point
    SopletterShapeType.CLOUD -> R.drawable.ic_sopletter_memo_cloud
}

internal fun SopletterMessage.memoColor(): Color = runCatching {
    Color(colorCode.toColorInt())
}.getOrDefault(DEFAULT_MEMO_COLOR)

private val DEFAULT_MEMO_COLOR = Color(0xFFC8E1FF)
