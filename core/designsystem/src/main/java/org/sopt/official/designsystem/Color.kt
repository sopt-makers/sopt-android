/*
 * MIT License
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
fun AlphaColor(
    color: Int,
    alphaPercentage: Int = 100
): Color {
    val alpha = (alphaPercentage / 100f * 255).toInt()
    return Color((color and 0x00FFFFFF) or (alpha shl 24))
}

// Blue Scale
val Blue600 = Color(0xFF2E60F6)
val Blue500 = Color(0xFF5070F7)
val Blue400 = Color(0xFF6380FC)
val Blue300 = Color(0xFF7F98FF)
val Blue200 = Color(0xFFA1B3FF)
val Blue100 = Color(0xFFC1CDFF)

// Gray Scale
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Gray900 = Color(0xFF151617)
val Gray800 = Color(0xFF353637)
val Gray700 = Color(0xFF535455)
val Gray600 = Color(0xFF666768)
val Gray500 = Color(0xFF8E8F90)
val Gray400 = Color(0xFFAEAFB0)
val Gray300 = Color(0xFFD3D4D5)
val Gray200 = Color(0xFFE4E5E6)
val Gray100 = Color(0xFFEFF0F1)
val Gray50 = Color(0xFFF7F8F9)

val Red = Color(0xFFD92B2B)

// MDS
val MdsGray950 = Color(0xFF0F1012)
