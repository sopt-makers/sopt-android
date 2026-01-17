/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.home.component

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import org.sopt.official.designsystem.Black40
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SuitMedium
import org.sopt.official.designsystem.SuitSemiBold
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel

@Composable
fun HomeGenerationChips(
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    modifier: Modifier = Modifier,
) {
    val recentChipColor = if (homeUserSoptLogDashboardModel.isActivated) Orange400 else Black40
    val recentChipTextColor = if (homeUserSoptLogDashboardModel.isActivated) colors.background else colors.onBackground

    Row(modifier = modifier) {
        RecentGenerationChip(
            chipColor = recentChipColor,
            textColor = recentChipTextColor,
            text = homeUserSoptLogDashboardModel.recentGenerationDescription,
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        LastGenerationChips(homeUserSoptLogDashboardModel.lastGenerations)
    }
}

@Composable
fun RecentGenerationChip(
    @ColorRes chipColor: Color,
    @ColorRes textColor: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Center,
        modifier = modifier.background(
            color = chipColor,
            shape = RoundedCornerShape(size = 15.dp),
        ).size(
            width = 82.dp,
            height = 24.dp,
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = SuitSemiBold, fontSize = 12.sp, lineHeight = 15.sp
            ),
            color = textColor,
        )
    }
}

@Composable
private fun LastGenerationChips(generations: ImmutableList<Long>) {
    generations.forEachIndexed { index, generation ->
        when (index) {
            0 -> GenerationChip(
                chipColor = colors.onSurface600,
                textColor = colors.onBackground,
                text = generation.toString(),
            )

            1 -> GenerationChip(
                chipColor = colors.onSurface700,
                textColor = colors.onSurface10,
                text = generation.toString(),
            )

            2 -> GenerationChip(
                chipColor = Transparent,
                textColor = colors.onSurface100,
                text = generation.toString(),
            )

            3 -> GenerationChip(
                chipColor = Transparent,
                textColor = colors.onSurface200,
                text = generation.toString(),
            )

            4 -> GenerationChip(
                chipColor = Transparent,
                textColor = colors.onSurface300,
                text = generation.toString(),
            )

            5 -> {
                GenerationChip(
                    chipColor = Transparent,
                    textColor = colors.onBackground,
                    text = "+1",
                )
                return@forEachIndexed
            }
        }
        Spacer(modifier = Modifier.width(width = 4.dp))
    }
}

@Composable
private fun GenerationChip(
    @ColorRes chipColor: Color,
    @ColorRes textColor: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Center,
        modifier = modifier.background(
            color = chipColor,
            shape = CircleShape,
        ).size(size = 24.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = SuitMedium, fontSize = 12.sp, lineHeight = 15.sp
            ),
            color = textColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeGenerationChipsPreview() {
    SoptTheme {
        HomeGenerationChips(
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel()
        )
    }
}
