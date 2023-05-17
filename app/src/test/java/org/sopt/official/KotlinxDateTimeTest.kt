package org.sopt.official

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.sopt.official.util.getGeneration

class KotlinxDateTimeTest {

    @Test
    @DisplayName("getGeneration 함수 파라메터에 SOPT 기수(1~32)를 넣게 되면 해당 기수의 시작 날짜를 반환한다")
    fun testGetGeneration() {
        // given
        val generation = 32
        val expectedDate = "2023-03-01".toLocalDate()

        // when
        val actualDate = getGeneration(generation)

        // then
        assertThat(actualDate).isEqualTo(expectedDate)
    }

    @Test
    @DisplayName("computeMothUntilNow 함수 파라메터에 SOPT 기수(1~32)를 넣게 되면 해당 기수의 시작 날짜부터 현재까지의 개월 수를 반환한다")
    fun testComputeMothUntilNow() {
        // given
        val generation = 32
        val currentTime = "2023-05-17".toLocalDate()
        val expectMonth = 2


        // when
        val actualMonth = computeMothUntilNow(generation, currentTime)

        // then
        assertThat(actualMonth).isEqualTo(expectMonth)
    }

    private fun computeMothUntilNow(generation: Int, currentTime: LocalDate): Int {
        val generationDate = getGeneration(generation)
        val diff = generationDate.periodUntil(currentTime)
        return diff.months + diff.years * 12
    }
}