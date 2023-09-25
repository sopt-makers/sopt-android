package org.sopt.official

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.sopt.official.util.calculateDurationOfGeneration
import org.sopt.official.util.calculateGenerationStartDate

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
            Arguments.of(30, "2022-03-01".toLocalDate()),
            Arguments.of(31, "2022-09-01".toLocalDate()),
            Arguments.of(32, "2023-03-01".toLocalDate()),
            Arguments.of(33, "2023-09-01".toLocalDate())
        )

        @JvmStatic
        fun generationLastMonthList() = listOf(
            Arguments.of(30, 14),
            Arguments.of(31, 8),
            Arguments.of(32, 2),
        )
    }
}