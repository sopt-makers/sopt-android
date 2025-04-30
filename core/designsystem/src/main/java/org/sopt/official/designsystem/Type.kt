/*
 * MIT License
 * Copyright 2022-2025 SOPT - Shout Our Passion Together
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
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

val PretendardBold = FontFamily(Font(R.font.pretendard_bold, FontWeight.Bold))
val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold, FontWeight.SemiBold))
val PretendardMedium = FontFamily(Font(R.font.pretendard_medium, FontWeight.Medium))
val PretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))
val PretendardLight = FontFamily(Font(R.font.pretendard_light, FontWeight.Light))

val SuitBold = FontFamily(Font(R.font.suit_bold, FontWeight.Bold))
val SuitSemiBold = FontFamily(Font(R.font.suit_semibold, FontWeight.SemiBold))
val SuitMedium = FontFamily(Font(R.font.suit_medium, FontWeight.Medium))
val SuitRegular = FontFamily(Font(R.font.suit_medium, FontWeight.Normal))
val SuitLight = FontFamily(Font(R.font.suit_medium, FontWeight.Light))

@Stable
class SoptTypography internal constructor(
    heading48B: TextStyle,
    heading40B: TextStyle,
    heading32B: TextStyle,
    heading28B: TextStyle,
    heading24B: TextStyle,
    heading20B: TextStyle,
    heading18B: TextStyle,
    heading16B: TextStyle,
    title32SB: TextStyle,
    title28SB: TextStyle,
    title24SB: TextStyle,
    title20SB: TextStyle,
    title18SB: TextStyle,
    title16SB: TextStyle,
    title14SB: TextStyle,
    body18M: TextStyle,
    body16M: TextStyle,
    body16R: TextStyle,
    body14M: TextStyle,
    body14R: TextStyle,
    body14L: TextStyle,
    body13M: TextStyle,
    body13R: TextStyle,
    body13L: TextStyle,
    body10M: TextStyle,
    label18SB: TextStyle,
    label16SB: TextStyle,
    label14SB: TextStyle,
    label12SB: TextStyle,
    label11SB: TextStyle,
) {
    var heading48B: TextStyle by mutableStateOf(heading48B)
        private set
    var heading40B: TextStyle by mutableStateOf(heading40B)
        private set
    var heading32B: TextStyle by mutableStateOf(heading32B)
        private set
    var heading28B: TextStyle by mutableStateOf(heading28B)
        private set
    var heading24B: TextStyle by mutableStateOf(heading24B)
        private set
    var heading20B: TextStyle by mutableStateOf(heading20B)
        private set
    var heading18B: TextStyle by mutableStateOf(heading18B)
        private set
    var heading16B: TextStyle by mutableStateOf(heading16B)
        private set
    var title32SB: TextStyle by mutableStateOf(title32SB)
        private set
    var title28SB: TextStyle by mutableStateOf(title28SB)
        private set
    var title24SB: TextStyle by mutableStateOf(title24SB)
        private set
    var title20SB: TextStyle by mutableStateOf(title20SB)
        private set
    var title18SB: TextStyle by mutableStateOf(title18SB)
        private set
    var title16SB: TextStyle by mutableStateOf(title16SB)
        private set
    var title14SB: TextStyle by mutableStateOf(title14SB)
        private set
    var body18M: TextStyle by mutableStateOf(body18M)
        private set
    var body16M: TextStyle by mutableStateOf(body16M)
        private set
    var body16R: TextStyle by mutableStateOf(body16R)
        private set
    var body14M: TextStyle by mutableStateOf(body14M)
        private set
    var body14R: TextStyle by mutableStateOf(body14R)
        private set
    var body14L: TextStyle by mutableStateOf(body14L)
        private set
    var body13M: TextStyle by mutableStateOf(body13M)
        private set
    var body13R: TextStyle by mutableStateOf(body13R)
        private set
    var body13L: TextStyle by mutableStateOf(body13L)
        private set
    var body10M: TextStyle by mutableStateOf(body10M)
        private set
    var label18SB: TextStyle by mutableStateOf(label18SB)
        private set
    var label16SB: TextStyle by mutableStateOf(label16SB)
        private set
    var label14SB: TextStyle by mutableStateOf(label14SB)
        private set
    var label12SB: TextStyle by mutableStateOf(label12SB)
        private set
    var label11SB: TextStyle by mutableStateOf(label11SB)
        private set

    fun copy(
        heading48B: TextStyle = this.heading48B,
        heading40B: TextStyle = this.heading40B,
        heading32B: TextStyle = this.heading32B,
        heading28B: TextStyle = this.heading28B,
        heading24B: TextStyle = this.heading24B,
        heading20B: TextStyle = this.heading20B,
        heading18B: TextStyle = this.heading18B,
        heading16B: TextStyle = this.heading16B,
        title32SB: TextStyle = this.title32SB,
        title28SB: TextStyle = this.title28SB,
        title24SB: TextStyle = this.title24SB,
        title20SB: TextStyle = this.title20SB,
        title18SB: TextStyle = this.title18SB,
        title16SB: TextStyle = this.title16SB,
        title14SB: TextStyle = this.title14SB,
        body18M: TextStyle = this.body18M,
        body16M: TextStyle = this.body16M,
        body16R: TextStyle = this.body16R,
        body14M: TextStyle = this.body14M,
        body14R: TextStyle = this.body14R,
        body14L: TextStyle = this.body14L,
        body13M: TextStyle = this.body13M,
        body13R: TextStyle = this.body13R,
        body13L: TextStyle = this.body13L,
        body10M: TextStyle = this.body10M,
        label18SB: TextStyle = this.label18SB,
        label16SB: TextStyle = this.label16SB,
        label14SB: TextStyle = this.label14SB,
        label12SB: TextStyle = this.label12SB,
        label11SB: TextStyle = this.label11SB,
    ): SoptTypography = SoptTypography(
        heading48B,
        heading40B,
        heading32B,
        heading28B,
        heading24B,
        heading20B,
        heading18B,
        heading16B,
        title32SB,
        title28SB,
        title24SB,
        title20SB,
        title18SB,
        title16SB,
        title14SB,
        body18M,
        body16M,
        body16R,
        body14M,
        body14R,
        body14L,
        body13M,
        body13R,
        body13L,
        body10M,
        label18SB,
        label16SB,
        label14SB,
        label12SB,
        label11SB,
    )

    fun update(other: SoptTypography) {
        heading48B = other.heading48B
        heading40B = other.heading40B
        heading32B = other.heading32B
        heading28B = other.heading28B
        heading24B = other.heading24B
        heading20B = other.heading20B
        heading18B = other.heading18B
        heading16B = other.heading16B
        title32SB = other.title32SB
        title28SB = other.title28SB
        title24SB = other.title24SB
        title20SB = other.title20SB
        title18SB = other.title18SB
        title16SB = other.title16SB
        title14SB = other.title14SB
        body18M = other.body18M
        body16M = other.body16M
        body16R = other.body16R
        body14M = other.body14M
        body14R = other.body14R
        body14L = other.body14L
        body13M = other.body13M
        body13R = other.body13R
        body13L = other.body13L
        body10M = other.body10M
        label18SB = other.label18SB
        label16SB = other.label16SB
        label14SB = other.label14SB
        label12SB = other.label12SB
        label11SB = other.label11SB
    }
}

@Stable
class LegacySoptTypography internal constructor(
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
    ): LegacySoptTypography = LegacySoptTypography(
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

    fun update(other: LegacySoptTypography) {
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
        heading48B = TextStyle(
            fontFamily = SuitBold,
            fontSize = 48.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 72.sp
        ),
        heading40B = TextStyle(
            fontFamily = SuitBold,
            fontSize = 40.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 72.sp
        ),
        heading32B = TextStyle(
            fontFamily = SuitBold,
            fontSize = 32.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 48.sp
        ),
        heading28B = TextStyle(
            fontFamily = SuitBold,
            fontSize = 28.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 42.sp
        ),
        heading24B = TextStyle(
            fontFamily = SuitBold,
            fontSize = 24.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 36.sp
        ),
        heading20B = TextStyle(
            fontFamily = SuitBold,
            fontSize = 20.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 30.sp
        ),
        heading18B = TextStyle(
            fontFamily = SuitBold,
            fontSize = 18.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 28.sp
        ),
        heading16B = TextStyle(
            fontFamily = SuitBold,
            fontSize = 16.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 24.sp
        ),
        title32SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 32.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 48.sp
        ),
        title28SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 28.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 42.sp
        ),
        title24SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 24.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 36.sp
        ),
        title20SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 20.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 30.sp
        ),

        title18SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 18.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 28.sp
        ),
        title16SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 16.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 24.sp
        ),
        title14SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 14.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 20.sp
        ),
        body18M = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 18.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 30.sp
        ),
        body16M = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 16.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 26.sp
        ),
        body16R = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 16.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 26.sp
        ),
        body14M = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 14.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 22.sp
        ),
        body14R = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 14.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 22.sp
        ),
        body14L = TextStyle(
            fontFamily = SuitLight,
            fontSize = 14.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 22.sp
        ),
        body13M = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 13.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 20.sp
        ),
        body13R = TextStyle(
            fontFamily = SuitRegular,
            fontSize = 13.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 20.sp
        ),
        body13L = TextStyle(
            fontFamily = SuitLight,
            fontSize = 13.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 20.sp
        ),
        body10M = TextStyle(
            fontFamily = SuitMedium,
            fontSize = 10.sp,
            letterSpacing = (-0.015).em,
            lineHeight = 20.sp
        ),
        label18SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 18.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp
        ),
        label16SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 16.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 22.sp
        ),
        label14SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 14.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 18.sp
        ),
        label12SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 12.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 16.sp
        ),
        label11SB = TextStyle(
            fontFamily = SuitSemiBold,
            fontSize = 11.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 14.sp
        ),
    )
}

@Composable
fun LegacySoptTypography(): LegacySoptTypography {
    return LegacySoptTypography(
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
