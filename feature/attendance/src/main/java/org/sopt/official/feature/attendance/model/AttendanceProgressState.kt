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
package org.sopt.official.feature.attendance.model

import org.sopt.official.domain.attendance.entity.SoptEvent

sealed class AttendanceProgressState {
    object BeforeAttendance : AttendanceProgressState()
    
    data class FirstAttendanceCompleted(
        val isAttended: Boolean
    ) : AttendanceProgressState()
    
    data class SecondAttendanceCompleted(
        val firstAttended: Boolean,
        val secondAttended: Boolean
    ) : AttendanceProgressState()
    
    companion object {
        fun fromSoptEvent(soptEvent: SoptEvent): AttendanceProgressState {
            return when (soptEvent.attendances.size) {
                0 -> BeforeAttendance
                1 -> {
                    val firstStatus = soptEvent.attendances[0].attendedAt != AttendanceConstants.FIRST_ATTENDANCE_TEXT
                    FirstAttendanceCompleted(isAttended = firstStatus)
                }
                2 -> {
                    val firstStatus = soptEvent.attendances[0].attendedAt != AttendanceConstants.FIRST_ATTENDANCE_TEXT
                    val secondStatus = soptEvent.attendances[1].attendedAt != AttendanceConstants.SECOND_ATTENDANCE_TEXT
                    SecondAttendanceCompleted(firstAttended = firstStatus, secondAttended = secondStatus)
                }
                else -> BeforeAttendance
            }
        }
    }
}

data class ProgressBarUIState(
    val isFirstProgressBarActive: Boolean = false,
    val isFirstProgressBarAttendance: Boolean = false,
    val isFirstToSecondLineActive: Boolean = false,
    val isSecondProgressBarActive: Boolean = false,
    val isSecondProgressBarAttendance: Boolean = false,
    val isSecondToThirdLineActive: Boolean = false,
    val isThirdProgressBarActive: Boolean = false,
    val isThirdProgressBarAttendance: Boolean = false,
    val isThirdProgressBarTardy: Boolean = false,
    val isThirdProgressBarBeforeAttendance: Boolean = false,
) {
    val isThirdProgressBarVisible: Boolean
        get() = isThirdProgressBarActive && isThirdProgressBarTardy
    
    val isThirdProgressBarActiveAndBeforeAttendance: Boolean
        get() = isThirdProgressBarActive && isThirdProgressBarBeforeAttendance
    
    companion object {
        fun fromProgressState(progressState: AttendanceProgressState): ProgressBarUIState {
            return when (progressState) {
                is AttendanceProgressState.BeforeAttendance -> {
                    ProgressBarUIState(
                        isThirdProgressBarBeforeAttendance = true
                    )
                }
                is AttendanceProgressState.FirstAttendanceCompleted -> {
                    ProgressBarUIState(
                        isFirstProgressBarActive = true,
                        isFirstProgressBarAttendance = progressState.isAttended,
                        isFirstToSecondLineActive = true,
                        isThirdProgressBarBeforeAttendance = true
                    )
                }
                is AttendanceProgressState.SecondAttendanceCompleted -> {
                    val finalAttendanceStatus = determineFinalAttendanceStatus(
                        progressState.firstAttended,
                        progressState.secondAttended
                    )
                    
                    ProgressBarUIState(
                        isFirstProgressBarActive = true,
                        isFirstProgressBarAttendance = progressState.firstAttended,
                        isFirstToSecondLineActive = true,
                        isSecondProgressBarActive = true,
                        isSecondProgressBarAttendance = progressState.secondAttended,
                        isSecondToThirdLineActive = true,
                        isThirdProgressBarActive = true,
                        isThirdProgressBarAttendance = finalAttendanceStatus.isAttendance,
                        isThirdProgressBarTardy = finalAttendanceStatus.isTardy,
                        isThirdProgressBarBeforeAttendance = finalAttendanceStatus.isBeforeAttendance
                    )
                }
            }
        }
        
        private fun determineFinalAttendanceStatus(
            firstAttended: Boolean,
            secondAttended: Boolean
        ): FinalAttendanceStatus {
            return when {
                firstAttended && secondAttended -> {
                    // 완전 출석
                    FinalAttendanceStatus(
                        isAttendance = true,
                        isTardy = false,
                        isBeforeAttendance = true
                    )
                }
                (firstAttended && !secondAttended) || (!firstAttended && secondAttended) -> {
                    // 지각
                    FinalAttendanceStatus(
                        isAttendance = true,
                        isTardy = true,
                        isBeforeAttendance = true
                    )
                }
                else -> {
                    // 결석
                    FinalAttendanceStatus(
                        isAttendance = false,
                        isTardy = false,
                        isBeforeAttendance = false
                    )
                }
            }
        }
        
        private data class FinalAttendanceStatus(
            val isAttendance: Boolean,
            val isTardy: Boolean,
            val isBeforeAttendance: Boolean
        )
    }
}