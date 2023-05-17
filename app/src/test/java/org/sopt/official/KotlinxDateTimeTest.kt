package org.sopt.official

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.sopt.official.util.computeMothUntilNow
import org.sopt.official.util.getGenerationStartDate

class KotlinxDateTimeTest {

    @Test
    @DisplayName("SOPT 기수(1~32)를 입력하면 해당 기수의 시작 날짜를 반환한다")
    fun testGetGeneration() {
        // given
        val generation = 32
        val expectedDate = "2023-03-01".toLocalDate()

        // when
        val actualDate = getGenerationStartDate(generation)

        // then
        assertThat(actualDate).isEqualTo(expectedDate)
    }

    @Test
    @DisplayName("SOPT 기수(1~32)를 입력하면 해당 기수의 시작 날짜부터 입력한 날짜까지의 개월 수를 반환한다")
    fun testComputeMothUntilNow() {
        // given
        val generation = 32 // 32기 시작 날짜: 2023-03-01
        val expectMonth = 2

        // when
        val actualMonth = computeMothUntilNow(generation, LocalDate(2023, 5, 17))

        // then
        assertThat(actualMonth).isEqualTo(expectMonth)
    }
}