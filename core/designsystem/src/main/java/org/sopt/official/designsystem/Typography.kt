/*
 * MIT License
 * Copyright 2022-2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val PretendardBold = FontFamily(Font(R.font.pretendard_bold, FontWeight.Bold))
val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold, FontWeight.SemiBold))
val PretendardMedium = FontFamily(Font(R.font.pretendard_medium, FontWeight.Medium))
val PretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))
val PretendardLight = FontFamily(Font(R.font.pretendard_light, FontWeight.Light))

@Stable
class SoptTypography internal constructor(
    h1: TextStyle,
    h2: TextStyle,
    h3: TextStyle,
    h4: TextStyle,
    h5: TextStyle,
    h6: TextStyle,
    h7: TextStyle,
    title1: TextStyle,
    title2: TextStyle,
    title3: TextStyle,
    title4: TextStyle,
    title5: TextStyle,
    title6: TextStyle,
    title7: TextStyle,
    body1: TextStyle,
    body2: TextStyle,
    body3: TextStyle,
    body4: TextStyle,
    label1: TextStyle,
    label2: TextStyle,
    label3: TextStyle,
    label4: TextStyle,
    label5: TextStyle
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
    var h6: TextStyle by mutableStateOf(h6)
        private set
    var h7: TextStyle by mutableStateOf(h7)
        private set
    var title1: TextStyle by mutableStateOf(title1)
        private set
    var title2: TextStyle by mutableStateOf(title2)
        private set
    var title3: TextStyle by mutableStateOf(title3)
        private set
    var title4: TextStyle by mutableStateOf(title4)
        private set
    var title5: TextStyle by mutableStateOf(title5)
        private set
    var title6: TextStyle by mutableStateOf(title6)
        private set
    var title7: TextStyle by mutableStateOf(title7)
        private set
    var body1: TextStyle by mutableStateOf(body1)
        private set
    var body2: TextStyle by mutableStateOf(body2)
        private set
    var body3: TextStyle by mutableStateOf(body3)
        private set
    var body4: TextStyle by mutableStateOf(body4)
        private set
    var label1: TextStyle by mutableStateOf(label1)
        private set
    var label2: TextStyle by mutableStateOf(label2)
        private set
    var label3: TextStyle by mutableStateOf(label3)
        private set
    var label4: TextStyle by mutableStateOf(label4)
        private set
    var label5: TextStyle by mutableStateOf(label5)
        private set

    fun copy(
        h1: TextStyle = this.h1,
        h2: TextStyle = this.h2,
        h3: TextStyle = this.h3,
        h4: TextStyle = this.h4,
        h5: TextStyle = this.h5,
        h6: TextStyle = this.h6,
        h7: TextStyle = this.h7,
        title1: TextStyle = this.title1,
        title2: TextStyle = this.title2,
        title3: TextStyle = this.title3,
        title4: TextStyle = this.title4,
        title5: TextStyle = this.title5,
        title6: TextStyle = this.title6,
        title7: TextStyle = this.title7,
        body1: TextStyle = this.body1,
        body2: TextStyle = this.body2,
        body3: TextStyle = this.body3,
        body4: TextStyle = this.body4,
        label1: TextStyle = this.label1,
        label2: TextStyle = this.label2,
        label3: TextStyle = this.label3,
        label4: TextStyle = this.label4,
        label5: TextStyle = this.label5
    ): SoptTypography = SoptTypography(
        h1,
        h2,
        h3,
        h4,
        h5,
        h6,
        h7,
        title1,
        title2,
        title3,
        title4,
        title5,
        title6,
        title7,
        body1,
        body2,
        body3,
        body4,
        label1,
        label2,
        label3,
        label4,
        label5
    )

    fun update(other: SoptTypography) {
        h1 = other.h1
        h2 = other.h2
        h3 = other.h3
        h4 = other.h4
        h5 = other.h5
        h6 = other.h6
        h7 = other.h7
        title1 = other.title1
        title2 = other.title2
        title3 = other.title3
        title4 = other.title4
        title5 = other.title5
        title6 = other.title6
        title7 = other.title7
        body1 = other.body1
        body2 = other.body2
        body3 = other.body3
        body4 = other.body4
        label1 = other.label1
        label2 = other.label2
        label3 = other.label3
        label4 = other.label4
        label5 = other.label5
    }
}

@Composable
fun SoptTypography(): SoptTypography {
    return SoptTypography(
        h1 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 48.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 72.sp
        ),
        h2 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 32.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 48.sp
        ),
        h3 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 28.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 42.sp
        ),
        h4 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 24.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 36.sp
        ),
        h5 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 30.sp
        ),
        h6 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 18.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 28.sp
        ),
        h7 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp
        ),
        title1 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 32.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 48.sp
        ),
        title2 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 28.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 42.sp
        ),
        title3 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 24.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 36.sp
        ),
        title4 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 30.sp
        ),
        title5 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 18.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 28.sp
        ),
        title6 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp
        ),
        title7 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 14.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp
        ),
        body1 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 18.sp,
            letterSpacing = 0.5.sp,
            lineHeight = 30.sp
        ),
        body2 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 16.sp,
            letterSpacing = 0.25.sp,
            lineHeight = 26.sp
        ),
        body3 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp,
            lineHeight = 22.sp
        ),
        body4 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 13.sp,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp
        ),
        label1 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 18.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp
        ),
        label2 = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 22.sp
        ),
        label3 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 14.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 20.sp
        ),
        label4 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 12.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 16.sp
        ),
        label5 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 11.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 14.sp
        )
    )
}
