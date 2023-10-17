/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.designsystem.component.ratingbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.util.MultiFormFactorPreviews

private const val DEFAULT_MAX = 3

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    maxStars: Int = DEFAULT_MAX,
    stars: Int,
    gapSize: Dp = 10.dp,
    selectedColor: Color = SoptTheme.colors.mint300,
    unselectedColor: Color = SoptTheme.colors.onSurface30
) {
    require(maxStars >= stars) {
        "RatingBar의 최대 별 갯수는 선택된 별 갯수보다 커야합니다. Max Stars: $maxStars, stars: $stars"
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gapSize)
    ) {
        repeat(maxStars) {
            val ordinalIndex = it + 1
            RatingIcon(
                icon = icon,
                tint = if (ordinalIndex > stars) unselectedColor else selectedColor
            )
        }
    }
}

@Composable
private fun RatingIcon(
    @DrawableRes icon: Int,
    tint: Color
) {
    Icon(
        painter = painterResource(id = icon),
        contentDescription = "Rating Icon",
        tint = tint
    )
}

@MultiFormFactorPreviews
@Composable
private fun RatingBarPreview() {
    SoptTheme {
        Box(
            contentAlignment = Alignment.Center
        ) {
            RatingBar(
                icon = R.drawable.ic_star,
                maxStars = 3,
                stars = 2,
                gapSize = 10.dp
            )
        }
    }
}
