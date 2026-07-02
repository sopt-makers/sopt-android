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
package org.sopt.official.feature.sopletter.component

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

internal fun Modifier.verticalScrollbar(
    scrollState: ScrollState,
    thumbWidth: Dp,
    thumbHeight: Dp,
    thumbColor: Color,
): Modifier = drawWithContent {
    drawContent()

    if (scrollState.maxValue <= 0) return@drawWithContent

    val thumbWidthPx = thumbWidth.toPx()
    val thumbHeightPx = thumbHeight.toPx()

    val viewportHeight = size.height
    val viewportWidth = size.width
    val visibleThumbHeight = minOf(thumbHeightPx, viewportHeight)
    val scrollProgress = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
    val thumbOffsetY = (viewportHeight - visibleThumbHeight) * scrollProgress
    val thumbOffset = Offset(
        x = viewportWidth - thumbWidthPx,
        y = thumbOffsetY,
    )

    drawRoundRect(
        color = thumbColor,
        topLeft = thumbOffset,
        size = Size(
            width = thumbWidthPx,
            height = visibleThumbHeight,
        ),
        cornerRadius = CornerRadius(thumbWidthPx / 2f),
    )
}
