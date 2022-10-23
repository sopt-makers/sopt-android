package org.sopt.official.designsystem.style

import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.sopt.official.R

val SuitExtraBold = FontFamily(Font(R.font.suit_extrabold, FontWeight.ExtraBold))
val SuitBold = FontFamily(Font(R.font.suit_bold, FontWeight.Bold))
val SuitMedium = FontFamily(Font(R.font.suit_medium, FontWeight.Medium))
val SuitRegular = FontFamily(Font(R.font.suit_regular, FontWeight.Normal))

@Stable
class SoptTypography internal constructor(
    h1: TextStyle,
    h2: TextStyle,
    h3: TextStyle,
    h4: TextStyle,
    h5: TextStyle,
    sub1: TextStyle,
    sub2: TextStyle,
    b1: TextStyle,
    b2: TextStyle,
    caption: TextStyle,
) {
    var h1: TextStyle by mutableStateOf(h1)
        private set
    var h2: TextStyle by mutableStateOf(h2)
        private set
    var h3: TextStyle by mutableStateOf(h3)
        private set
    var h4: TextStyle by mutableStateOf(h4)
        private set
    var h5: TextStyle by mutableStateOf(h5)
        private set
    var sub1: TextStyle by mutableStateOf(sub1)
        private set
    var sub2: TextStyle by mutableStateOf(sub2)
        private set
    var b1: TextStyle by mutableStateOf(b1)
        private set
    var b2: TextStyle by mutableStateOf(b2)
        private set
    var caption: TextStyle by mutableStateOf(caption)
        private set

    fun copy(
        h1: TextStyle = this.h1,
        h2: TextStyle = this.h2,
        h3: TextStyle = this.h3,
        h4: TextStyle = this.h4,
        h5: TextStyle = this.h5,
        sub1: TextStyle = this.sub1,
        sub2: TextStyle = this.sub2,
        b1: TextStyle = this.b1,
        b2: TextStyle = this.b2,
        caption: TextStyle = this.caption,
    ): SoptTypography = SoptTypography(h1, h2, h3, h4, h5, sub1, sub2, b1, b2, caption)

    fun update(other: SoptTypography) {
        h1 = other.h1
        h2 = other.h2
        h3 = other.h3
        h4 = other.h4
        h5 = other.h5
        sub1 = other.sub1
        sub2 = other.sub2
        b1 = other.b1
        b2 = other.b2
        caption = other.caption
    }
}

@Composable
fun SoptTypography(): SoptTypography {
    return SoptTypography(
        h1 = TextStyle(
            fontFamily = SuitExtraBold,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        ),
        h2 = TextStyle(
            fontFamily = SuitBold,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        h3 = TextStyle(
            fontFamily = SuitBold,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        h4 = TextStyle(
            fontFamily = SuitBold,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
        h5 = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        ),
        sub1 = TextStyle(
            fontFamily = SuitBold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        sub2 = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        ),
        b1 = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        b2 = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        ),
        caption = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    )
}
