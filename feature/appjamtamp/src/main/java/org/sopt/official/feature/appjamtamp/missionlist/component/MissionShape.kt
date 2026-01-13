/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.missionlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.missionlist.model.MissionPattern

internal class MissionShape(
    private val patternCount: Int,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline = Outline.Generic(drawMissionPatternPath(size))

    private fun drawMissionPatternPath(size: Size): Path {
        val pattern = MissionPattern(calculatePatternLength(size))
        return drawMissionPatternPath(size, pattern)
    }

    private fun calculatePatternLength(size: Size): Float {
        return size.width * TOTAL_PATTERN_LENGTH / patternCount
    }

    private fun drawMissionPatternPath(
        size: Size,
        pattern: MissionPattern,
    ): Path =
        Path().apply {
            val sideSize = size.width * SIDE_SIZE_RATIO
            reset()
            moveTo(0f, 0f)
            lineTo(sideSize, 0f)
            drawTopMissionPattern(size, pattern)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(size.width - sideSize, size.height)
            drawBottomMissionPattern(size, pattern)
            lineTo(0f, size.height)
            lineTo(0f, 0f)
            close()
        }

    private fun Path.drawTopMissionPattern(
        size: Size,
        pattern: MissionPattern,
    ) {
        val sideSize = size.width * SIDE_SIZE_RATIO
        val rectHeight = pattern.diameter / 2
        for (i in 1..patternCount) {
            lineTo(sideSize + pattern.length * (i - 1) + pattern.gap, 0f)
            arcTo(
                rect =
                    Rect(
                        topLeft =
                            Offset(
                                x = sideSize + pattern.length * (i - 1) + pattern.gap,
                                y = -rectHeight,
                            ),
                        bottomRight =
                            Offset(
                                x = sideSize + pattern.length * (i - 1) + pattern.gap + pattern.diameter,
                                y = rectHeight,
                            ),
                    ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false,
            )
            lineTo(sideSize + pattern.length * i, 0f)
        }
    }

    private fun Path.drawBottomMissionPattern(
        size: Size,
        pattern: MissionPattern,
    ) {
        val sideSize = size.width * SIDE_SIZE_RATIO
        val rectHeight = pattern.diameter / 2
        for (i in 1..patternCount) {
            lineTo(size.width - (sideSize + pattern.length * (i - 1) + pattern.gap), size.height)
            arcTo(
                rect =
                    Rect(
                        topLeft =
                            Offset(
                                x = size.width - (sideSize + pattern.gap + pattern.length * (i - 1) + pattern.diameter),
                                y = size.height - rectHeight,
                            ),
                        bottomRight =
                            Offset(
                                x = size.width - (sideSize + pattern.gap + pattern.length * (i - 1)),
                                y = size.height + rectHeight,
                            ),
                    ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false,
            )
            lineTo(size.width - (sideSize + pattern.length * i), size.height)
        }
    }

    companion object {
        val DEFAULT_WAVE: MissionShape = MissionShape(6)
        private const val TOTAL_PATTERN_LENGTH = 0.9f
        private const val SIDE_SIZE_RATIO: Float = 0.05f
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMissionShape() {
    SoptTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val shape = MissionShape.DEFAULT_WAVE
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.6f),
                shape = shape,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(shape = shape, color = Color.Green),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Ticket Price : 20$",
                        modifier = Modifier.padding(30.dp),
                        color = Color.Black,
                        style = SoptTheme.typography.heading20B
                    )
                }
            }
        }
    }
}
