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
package org.sopt.official.feature.sopletter.main.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

enum class SopletterMemoRotationType(
    val degree: Float,
) {
    LEFT(-10f),
    CENTER(0f),
    RIGHT(10f),
}

enum class SopletterMemoColor(
    val color: Color,
) {
    BLUE(Color(0xFFC8E1FF)),
    MINT(Color(0xFFCCFFEC)),
    PINK(Color(0xFFFFD1D3)),
    YELLOW(Color(0xFFFFF4D4)),
}

// TODO 서버 스펙에 맞게 추후 수정 예정
data class SopletterMemoUiModel(
    val id: Long,
    val message: String,
    @param:DrawableRes val shapeImageRes: Int,
    val rotation: SopletterMemoRotationType,
    val memoColor: SopletterMemoColor,
)
