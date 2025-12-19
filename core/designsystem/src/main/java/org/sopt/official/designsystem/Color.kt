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
@file:Suppress("FunctionName")

package org.sopt.official.designsystem

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
fun AlphaColor(color: Int, alphaPercentage: Int = 100): Color {
    val alpha = (alphaPercentage / 100f * 255).toInt()
    return Color((color and 0x00FFFFFF) or (alpha shl 24))
}

// Black Scale
val Black40 = Color(0xFF3C3D40)

// Gray Scale
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Black80 = Color(0xFF1C1D1E)
val Gray950 = Color(0xFF0F1012)
val Gray900 = Color(0xFF17181C)
val Gray800 = Color(0xFF202025)
val Gray700 = Color(0xFF2E2E35)
val Gray600 = Color(0xFF3F3F47)
val Gray500 = Color(0xFF515159)
val Gray400 = Color(0xFF66666D)
val Gray300 = Color(0xFF808087)
val Gray200 = Color(0xFF9D9DA4)
val Gray100 = Color(0xFFC3C3C6)
val Gray80 = Color(0xFF838383)
val Gray60 = Color(0xFF989BA0)
val Gray50 = Color(0xFFE4E4E5)
val Gray30 = Color(0xFFF0F0F0)
val Gray10 = Color(0xFFFCFCFC)

// Blue Scale
val Blue900 = Color(0xFF20274D)
val Blue800 = Color(0xFF23306A)
val Blue700 = Color(0xFF253B8C)
val Blue600 = Color(0xFF2649B3)
val Blue500 = Color(0xFF2C53DF)
val Blue400 = Color(0xFF346FFA)
val Blue300 = Color(0xFF4485FF)
val Blue200 = Color(0xFF619EFF)
val Blue100 = Color(0xFF8FC0FF)
val Blue50 = Color(0xFFC8E1FF)

// Red Scale
val Red900 = Color(0xFF3C2020)
val Red800 = Color(0xFF562025)
val Red700 = Color(0xFF7A242D)
val Red600 = Color(0xFF9E2733)
val Red500 = Color(0xFFCA2F3D)
val Red400 = Color(0xFFF04251)
val Red300 = Color(0xFFFA616D)
val Red200 = Color(0xFFFE818B)
val Red100 = Color(0xFFFFA8AD)

// Green Scale
val Green900 = Color(0xFF15372B)
val Green800 = Color(0xFF13533C)
val Green700 = Color(0xFF136D4C)
val Green600 = Color(0xFF138A5E)
val Green500 = Color(0xFF13A06C)
val Green400 = Color(0xFF16BF81)
val Green300 = Color(0xFF26CF91)
val Green200 = Color(0xFF4EE4AD)
val Green100 = Color(0xFF82F6CB)

// Yellow Scale
val Yellow900 = Color(0xFF534123)
val Yellow800 = Color(0xFF72531E)
val Yellow700 = Color(0xFFB57B1D)
val Yellow600 = Color(0xFFEBA01E)
val Yellow500 = Color(0xFFFFB326)
val Yellow400 = Color(0xFFFFC234)
val Yellow300 = Color(0xFFFFCD59)
val Yellow200 = Color(0xFFFFDE8A)
val Yellow50 = Color(0xFFFFF4D4)

// Orange Scale
val Orange900 = Color(0xFF422109)
val Orange800 = Color(0xFF5C2B0C)
val Orange700 = Color(0xFF853D11)
val Orange600 = Color(0xFFAD4E17)
val Orange500 = Color(0xFFD4591C)
val Orange400 = Color(0xFFF77234)
val Orange300 = Color(0xFFFF834A)
val Orange200 = Color(0xFFFFA480)
val Orange100 = Color(0xFFFFCEBD)

// Purple Scale
val Purple300 = Color(0xFFC292FF)
val Purple200 = Color(0xFFEEE1FF)
val Purple100 = Color(0xFFFAF6FF)

// Pink Scale
val Pink300 = Color(0xFFFF7EAD)
val Pink200 = Color(0xFFFFD3E3)
val Pink100 = Color(0xFFFFF1F6)

// Mint Scale
val Mint300 = Color(0xFF53FFC0)
val Mint200 = Color(0xFFD3FFF0)
val Mint100 = Color(0xFFE5FFF6)

// Red-Alpha Scale
val RedAlpha900 = AlphaColor(0xF04251, 90)
val RedAlpha800 = AlphaColor(0xF04251, 80)
val RedAlpha700 = AlphaColor(0xF04251, 70)
val RedAlpha600 = AlphaColor(0xF04251, 60)
val RedAlpha500 = AlphaColor(0xF04251, 50)
val RedAlpha400 = AlphaColor(0xF04251, 40)
val RedAlpha300 = AlphaColor(0xF04251, 30)
val RedAlpha200 = AlphaColor(0xF04251, 20)
val RedAlpha100 = AlphaColor(0xF04251, 10)

// Orange-Alpha Scale
val OrangeAlpha900 = AlphaColor(0xFF77234, 90)
val OrangeAlpha800 = AlphaColor(0xFF77234, 80)
val OrangeAlpha700 = AlphaColor(0xFF77234, 70)
val OrangeAlpha600 = AlphaColor(0xFF77234, 60)
val OrangeAlpha500 = AlphaColor(0xFF77234, 50)
val OrangeAlpha400 = AlphaColor(0xFF77234, 40)
val OrangeAlpha300 = AlphaColor(0xFF77234, 30)
val OrangeAlpha200 = AlphaColor(0xFF77234, 20)
val OrangeAlpha100 = AlphaColor(0xFF77234, 10)

// Blue-Alpha Scale
val BlueAlpha900 = AlphaColor(0x346FFA, 90)
val BlueAlpha800 = AlphaColor(0x346FFA, 80)
val BlueAlpha700 = AlphaColor(0x346FFA, 70)
val BlueAlpha600 = AlphaColor(0x346FFA, 60)
val BlueAlpha500 = AlphaColor(0x346FFA, 50)
val BlueAlpha400 = AlphaColor(0x346FFA, 40)
val BlueAlpha300 = AlphaColor(0x346FFA, 30)
val BlueAlpha200 = AlphaColor(0x346FFA, 20)
val BlueAlpha100 = AlphaColor(0x346FFA, 10)

// Gray-Alpha Scale
val GrayAlpha900 = AlphaColor(0x0F0F12, 90)
val GrayAlpha800 = AlphaColor(0x0F0F12, 80)
val GrayAlpha700 = AlphaColor(0x0F0F12, 70)
val GrayAlpha600 = AlphaColor(0x0F0F12, 60)
val GrayAlpha500 = AlphaColor(0x0F0F12, 50)
val GrayAlpha400 = AlphaColor(0x0F0F12, 40)
val GrayAlpha300 = AlphaColor(0x0F0F12, 30)
val GrayAlpha200 = AlphaColor(0x0F0F12, 20)
val GrayAlpha100 = AlphaColor(0x0F0F12, 10)

// MDS
val MdsGray950 = Color(0xFF0F1012)
