/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
@file:Suppress("NonAsciiCharacters")

package org.sopt.official.designsystem

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class ColorAlphaArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(100, 0xFFFFFFFF),
            Arguments.of(90, 0xE5FFFFFF),
            Arguments.of(80, 0xCCFFFFFF),
            Arguments.of(70, 0xB2FFFFFF),
            Arguments.of(60, 0x99FFFFFF),
            Arguments.of(50, 0x7FFFFFFF),
            Arguments.of(40, 0x66FFFFFF),
            Arguments.of(30, 0x4CFFFFFF),
            Arguments.of(20, 0x33FFFFFF),
            Arguments.of(10, 0x19FFFFFF),
            Arguments.of(0, 0x00FFFFFF)
        )
    }
}

class ColorTest {
    @ParameterizedTest
    @ArgumentsSource(ColorAlphaArgumentsProvider::class)
    fun alphaColorCompatibilityTest(alpha: Int, expectedHex: Long) {
        // given
        val expected = Color(expectedHex)

        // when
        val colorHex = 0xFFFFFF
        val actual = AlphaColor(colorHex, alpha)

        assertThat(actual).isEqualTo(expected)
    }
}
