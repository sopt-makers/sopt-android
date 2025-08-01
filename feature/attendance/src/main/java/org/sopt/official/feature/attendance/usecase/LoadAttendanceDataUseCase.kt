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
package org.sopt.official.feature.attendance.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.sopt.official.domain.attendance.entity.AttendanceHistory
import org.sopt.official.domain.attendance.entity.AttendanceRound
import org.sopt.official.domain.attendance.entity.SoptEvent
import org.sopt.official.domain.attendance.repository.AttendanceRepository
import org.sopt.official.feature.attendance.model.AttendanceUiState
import javax.inject.Inject

/**
 * 출석 데이터 로드 UseCase
 * - 병렬 데이터 로드
 * - 비즈니스 로직 캡슐화
 * - 테스트 용이성
 */
class LoadAttendanceDataUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val mapToUiStateUseCase: MapToAttendanceUiStateUseCase
) {
    
    suspend operator fun invoke(): Result<AttendanceUiState> = runCatching {
        coroutineScope {
            // 병렬로 기본 데이터 로드
            val soptEventDeferred = async { attendanceRepository.fetchSoptEvent() }
            val historyDeferred = async { attendanceRepository.fetchAttendanceHistory() }
            
            val soptEvent = soptEventDeferred.await().getOrThrow()
            val history = historyDeferred.await().getOrThrow()
            
            // AttendanceRound 로드 (실패해도 진행)
            val attendanceRound = loadAttendanceRound(soptEvent.id.toLong())
            
            // UI 상태로 변환
            mapToUiStateUseCase(soptEvent, history, attendanceRound)
        }
    }
    
    private suspend fun loadAttendanceRound(eventId: Long): AttendanceRound? {
        return try {
            attendanceRepository.fetchAttendanceRound(eventId).getOrNull()
        } catch (error: Throwable) {
            null // AttendanceRound 실패는 전체 프로세스에 영향 주지 않음
        }
    }
}