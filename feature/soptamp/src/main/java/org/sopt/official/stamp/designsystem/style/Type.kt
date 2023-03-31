/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.designsystem.style

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
import org.sopt.official.stamp.R

val PretendardBold = FontFamily(Font(R.font.pretendard_bold, FontWeight.Bold))
val PretendardMedium = FontFamily(Font(R.font.pretendard_medium, FontWeight.Medium))
val PretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))
val MontserratBold = FontFamily(Font(R.font.montserrat_bold, FontWeight.Bold))
val MontserratRegular = FontFamily(Font(R.font.montserrat_regular, FontWeight.Normal))

@Stable
class SoptTypography internal constructor(
    h1: TextStyle,
    h2: TextStyle,
    h3: TextStyle,
    h4: TextStyle,
    sub1: TextStyle,
    sub2: TextStyle,
    sub3: TextStyle,
    sub4: TextStyle,
    caption1: TextStyle,
    caption2: TextStyle,
    caption3: TextStyle,
    caption4: TextStyle
) {
    var h1: TextStyle by mutableStateOf(h1)
        private set
    var h2: TextStyle by mutableStateOf(h2)
        private set
    var h3: TextStyle by mutableStateOf(h3)
        private set
    var h4: TextStyle by mutableStateOf(h4)
        private set
    var sub1: TextStyle by mutableStateOf(sub1)
        private set
    var sub2: TextStyle by mutableStateOf(sub2)
        private set
    var sub3: TextStyle by mutableStateOf(sub3)
        private set
    var sub4: TextStyle by mutableStateOf(sub4)
        private set
    var caption1: TextStyle by mutableStateOf(caption1)
        private set
    var caption2: TextStyle by mutableStateOf(caption2)
        private set
    var caption3: TextStyle by mutableStateOf(caption3)
        private set
    var caption4: TextStyle by mutableStateOf(caption4)
        private set

    fun copy(
        h1: TextStyle = this.h1,
        h2: TextStyle = this.h2,
        h3: TextStyle = this.h3,
        h4: TextStyle = this.h4,
        sub1: TextStyle = this.sub1,
        sub2: TextStyle = this.sub2,
        sub3: TextStyle = this.sub3,
        sub4: TextStyle = this.sub4,
        caption1: TextStyle = this.caption1,
        caption2: TextStyle = this.caption2,
        caption3: TextStyle = this.caption3,
        caption4: TextStyle = this.caption4
    ): SoptTypography = SoptTypography(h1, h2, h3, h4, sub1, sub2, sub3, sub4, caption1, caption2, caption3, caption4)

    fun update(other: SoptTypography) {
        h1 = other.h1
        h2 = other.h2
        h3 = other.h3
        h4 = other.h4
        sub1 = other.sub1
        sub2 = other.sub2
        sub3 = other.sub3
        sub4 = other.sub4
        caption1 = other.caption1
        caption2 = other.caption2
        caption3 = other.caption3
        caption4 = other.caption4
    }
}

@Composable
fun SoptTypography(): SoptTypography {
    return SoptTypography(
        h1 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        h2 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
        h3 = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        h4 = TextStyle(
            fontFamily = MontserratBold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        sub1 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        ),
        sub2 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        sub3 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        ),
        sub4 = TextStyle(
            fontFamily = MontserratRegular,
            fontSize = 30.sp,
            fontWeight = FontWeight.Normal
        ),
        caption1 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        ),
        caption2 = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        ),
        caption3 = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        ),
        caption4 = TextStyle(
            fontFamily = MontserratRegular,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    )
}
