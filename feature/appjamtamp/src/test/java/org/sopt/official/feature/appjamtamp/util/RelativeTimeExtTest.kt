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
package org.sopt.official.feature.appjamtamp.util

import com.google.common.truth.Truth.assertThat
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class RelativeTimeExtTest {

    @ParameterizedTest
    @MethodSource("relativeTimeCases")
    @DisplayName("createdAt 문자열을 상대 시간 문구로 변환한다")
    fun toRelativeTime(description: String, createdAt: String?, expected: String) {
        // when
        val actual = createdAt.toRelativeTime(currentDateTime = fixedNow)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        private val fixedNow: ZonedDateTime =
            ZonedDateTime.of(2026, 6, 3, 12, 0, 0, 0, ZoneId.of("Asia/Seoul"))

        @JvmStatic
        fun relativeTimeCases() = listOf(
            Arguments.of("null 입력은 빈 문자열을 반환한다", null, ""),
            Arguments.of("blank 입력은 빈 문자열을 반환한다", "", ""),
            Arguments.of("파싱 불가능한 입력은 빈 문자열을 반환한다", "invalid", ""),
            Arguments.of("미래 시각은 방금 전으로 표시한다", "2026-06-03T12:01:00", "방금 전"),
            Arguments.of("10분 미만은 방금 전으로 표시한다", "2026-06-03T11:50:01", "방금 전"),
            Arguments.of("10분 경계는 분 전으로 표시한다", "2026-06-03T11:50:00", "10분 전"),
            Arguments.of("1시간 미만은 분 전으로 표시한다", "2026-06-03T11:01:00", "59분 전"),
            Arguments.of("1시간 경계는 시간 전으로 표시한다", "2026-06-03T11:00:00", "1시간 전"),
            Arguments.of("24시간 미만은 시간 전으로 표시한다", "2026-06-02T12:01:00", "23시간 전"),
            Arguments.of("24시간 경계는 일 전으로 표시한다", "2026-06-02T12:00:00", "1일 전"),
            Arguments.of("7일 미만은 일 전으로 표시한다", "2026-05-27T12:00:01", "6일 전"),
            Arguments.of("7일 경계는 주 전으로 표시한다", "2026-05-27T12:00:00", "1주 전"),
            Arguments.of("28일 미만은 주 전으로 표시한다", "2026-05-06T12:00:01", "3주 전"),
            Arguments.of("28일 경계는 월일 포맷으로 표시한다", "2026-05-06T12:00:00", "5월 6일"),
        )
    }
}
