/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.attendance.model

import org.sopt.official.domain.attendance.entity.AttendanceHistory
import org.sopt.official.domain.attendance.entity.SoptEvent

/**
 * 출석 화면의 모든 UI 상태를 하나로 통합한 데이터 클래스
 * - 상태 응집성 개선
 * - 단일 진실 공급원 (Single Source of Truth)
 * - 테스트 용이성 증대
 */
data class AttendanceUiState(
    val isLoading: Boolean = false,
    val soptEvent: SoptEvent? = null,
    val attendanceHistory: AttendanceHistory? = null,
    val progressState: ProgressBarUIState = ProgressBarUIState(),
    val buttonState: AttendanceButtonState = AttendanceButtonState(),
    val dialogState: AttendanceDialogState = AttendanceDialogState.Hidden,
    val error: String? = null
) {
    /**
     * 데이터 로딩이 완료되었는지 확인
     */
    val isDataReady: Boolean
        get() = !isLoading && soptEvent != null && attendanceHistory != null && error == null

    /**
     * 에러 상태인지 확인
     */
    val hasError: Boolean
        get() = error != null

    companion object {
        /**
         * 초기 상태
         */
        val Initial = AttendanceUiState(isLoading = true)

        /**
         * 에러 상태 생성
         */
        fun error(message: String) = AttendanceUiState(
            isLoading = false,
            error = message
        )

        /**
         * 성공 상태 생성
         */
        fun success(
            soptEvent: SoptEvent,
            attendanceHistory: AttendanceHistory,
            progressState: ProgressBarUIState,
            buttonState: AttendanceButtonState
        ) = AttendanceUiState(
            isLoading = false,
            soptEvent = soptEvent,
            attendanceHistory = attendanceHistory,
            progressState = progressState,
            buttonState = buttonState
        )
    }
}

/**
 * 출석 버튼 상태
 */
data class AttendanceButtonState(
    val isVisible: Boolean = false,
    val isEnabled: Boolean = false,
    val text: String = ""
) {
    companion object {
        val Hidden = AttendanceButtonState()

        fun visible(text: String, isEnabled: Boolean = true) = AttendanceButtonState(
            isVisible = true,
            isEnabled = isEnabled,
            text = text
        )
    }
}

/**
 * 출석 다이얼로그 상태
 */
sealed class AttendanceDialogState {
    object Hidden : AttendanceDialogState()

    data class CodeInput(
        val title: String
    ) : AttendanceDialogState()

    data class Error(
        val title: String,
        val message: String
    ) : AttendanceDialogState()
}

/**
 * 출석 라운드 상태 - 비즈니스 로직 분리
 */
sealed class AttendanceRoundState {
    object NoSession : AttendanceRoundState()
    object BeforeTime : AttendanceRoundState()
    object AfterTime : AttendanceRoundState()

    data class Available(
        val subLectureId: Long,
        val roundText: String
    ) : AttendanceRoundState()

    data class Completed(
        val roundText: String
    ) : AttendanceRoundState()

    companion object {
        /**
         * AttendanceRound ID를 기반으로 상태 결정
         */
        fun fromRoundId(
            id: Long,
            roundText: String,
            userAttendanceCount: Int
        ): AttendanceRoundState {
            return when (id) {
                AttendanceConstants.ERROR_CODE -> NoSession
                AttendanceConstants.NO_SESSION_CODE -> NoSession
                AttendanceConstants.TIME_RESTRICTION_CODE -> BeforeTime
                else -> {
                    val expectedAttendanceCount = roundText.firstOrNull()?.digitToIntOrNull() ?: 0
                    if (userAttendanceCount >= expectedAttendanceCount) {
                        Completed(roundText)
                    } else {
                        Available(id, roundText)
                    }
                }
            }
        }
    }
}
