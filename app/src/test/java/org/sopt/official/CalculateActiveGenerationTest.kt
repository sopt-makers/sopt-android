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
package org.sopt.official

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.sopt.official.common.util.calculateDurationOfGeneration
import org.sopt.official.common.util.calculateGenerationStartDate

class CalculateActiveGenerationTest {

    @ParameterizedTest
    @MethodSource("generationStartDateList")
    @DisplayName("SOPT 기수(1~32)를 입력하면 해당 기수의 시작 날짜를 반환한다")
    fun testGetGeneration(generation: Int, expectedDate: LocalDate) {
        // when
        val actualDate = calculateGenerationStartDate(generation)

        // then
        assertThat(actualDate).isEqualTo(expectedDate)
    }

    @ParameterizedTest
    @MethodSource("generationLastMonthList")
    @DisplayName("SOPT 기수(1~32)를 입력하면 해당 기수의 시작 날짜부터 입력한 날짜까지의 개월 수를 반환한다 (30~32기 테스트)")
    fun testComputeMothUntilNow(generation: Int, expectMonth: Int) {
        // given
        val actualDate = calculateGenerationStartDate(generation)

        // when
        val actualMonth = calculateDurationOfGeneration(actualDate, LocalDate(2023, 5, 17))

        // then
        assertThat(actualMonth).isEqualTo(expectMonth)
    }

    companion object {
        @JvmStatic
        fun generationStartDateList() = listOf(
            Arguments.of(30, LocalDate.parse("2022-03-01")),
            Arguments.of(31, LocalDate.parse("2022-09-01")),
            Arguments.of(32, LocalDate.parse("2023-03-01")),
            Arguments.of(33, LocalDate.parse("2023-09-01"))
        )

        @JvmStatic
        fun generationLastMonthList() = listOf(
            Arguments.of(30, 14),
            Arguments.of(31, 8),
            Arguments.of(32, 2),
        )
    }
}
