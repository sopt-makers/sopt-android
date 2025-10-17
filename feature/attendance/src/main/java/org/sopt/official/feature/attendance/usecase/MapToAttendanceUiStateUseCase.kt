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
package org.sopt.official.feature.attendance.usecase

import org.sopt.official.domain.attendance.entity.AttendanceHistory
import org.sopt.official.domain.attendance.entity.AttendanceRound
import org.sopt.official.domain.attendance.entity.SoptEvent
import org.sopt.official.feature.attendance.model.AttendanceButtonState
import org.sopt.official.feature.attendance.model.AttendanceProgressState
import org.sopt.official.feature.attendance.model.AttendanceRoundState
import org.sopt.official.feature.attendance.model.AttendanceUiState
import org.sopt.official.feature.attendance.model.ProgressBarUIState
import javax.inject.Inject

/**
 * 도메인 엔티티를 AttendanceUiState로 변환하는 UseCase
 * - 단일 책임: 데이터 변환만 담당
 * - 순수 함수: 사이드 이펙트 없음
 * - 테스트 용이성: 입력/출력이 명확
 */
class MapToAttendanceUiStateUseCase @Inject constructor() {

    operator fun invoke(
        soptEvent: SoptEvent,
        attendanceHistory: AttendanceHistory,
        attendanceRound: AttendanceRound? = null
    ): AttendanceUiState {
        val progressState = createProgressState(soptEvent)
        val buttonState = createButtonState(attendanceRound, soptEvent.attendances.size)

        return AttendanceUiState.success(
            soptEvent = soptEvent,
            attendanceHistory = attendanceHistory,
            progressState = progressState,
            buttonState = buttonState
        )
    }

    /**
     * 진행 상태 생성
     */
    private fun createProgressState(soptEvent: SoptEvent): ProgressBarUIState {
        val progressState = AttendanceProgressState.fromSoptEvent(soptEvent)
        return ProgressBarUIState.fromProgressState(progressState)
    }

    /**
     * 버튼 상태 생성
     */
    private fun createButtonState(
        attendanceRound: AttendanceRound?,
        userAttendanceCount: Int
    ): AttendanceButtonState {
        if (attendanceRound == null) {
            return AttendanceButtonState.Hidden
        }

        val roundState = AttendanceRoundState.fromRoundId(
            id = attendanceRound.id,
            roundText = attendanceRound.roundText,
            userAttendanceCount = userAttendanceCount
        )

        return when (roundState) {
            is AttendanceRoundState.NoSession -> AttendanceButtonState.Hidden

            is AttendanceRoundState.BeforeTime -> AttendanceButtonState.visible(
                text = attendanceRound.roundText,
                isEnabled = false
            )

            is AttendanceRoundState.AfterTime -> AttendanceButtonState.visible(
                text = "출석이 이미 종료되었습니다",
                isEnabled = false
            )

            is AttendanceRoundState.Available -> AttendanceButtonState.visible(
                text = roundState.roundText,
                isEnabled = true
            )

            is AttendanceRoundState.Completed -> AttendanceButtonState.visible(
                text = "${roundState.roundText.take(5)} 종료",
                isEnabled = false
            )
        }
    }
}
