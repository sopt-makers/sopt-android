/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.v2.onboarding.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun PokeOnboardingPageIndicator(
    numberOfPages: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    selectedColor: Color = SoptTheme.colors.fg.neutral.bold,
    defaultColor: Color = SoptTheme.colors.fg.neutral.ghostDisabled,
    animationDurationInMillis: Int = 300,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier,
    ) {
        repeat(numberOfPages) { page ->
            PokeOnboardingPageIndicatorDot(
                isSelected = page == pagerState.currentPage,
                selectedColor = selectedColor,
                defaultColor = defaultColor,
                animationDurationInMillis = animationDurationInMillis,
            )
        }
    }
}

@Composable
private fun PokeOnboardingPageIndicatorDot(
    isSelected: Boolean,
    selectedColor: Color,
    defaultColor: Color,
    animationDurationInMillis: Int,
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) selectedColor else defaultColor,
        animationSpec = tween(durationMillis = animationDurationInMillis),
        label = "color",
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .background(color = color, shape = CircleShape),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1012)
@Composable
private fun PokeOnboardingPageIndicatorPreview() {
    SoptTheme {
        PokeOnboardingPageIndicator(
            numberOfPages = 3,
            pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 }),
        )
    }
}
