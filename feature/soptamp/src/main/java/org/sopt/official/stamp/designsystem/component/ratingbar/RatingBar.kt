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
