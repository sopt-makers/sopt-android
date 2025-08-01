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
package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.attendance.entity.AttendanceHistory
import org.sopt.official.domain.attendance.entity.AttendanceRound
import org.sopt.official.domain.attendance.entity.SoptEvent
import org.sopt.official.domain.attendance.repository.AttendanceRepository
import org.sopt.official.feature.attendance.model.AttendanceConstants
import org.sopt.official.feature.attendance.model.AttendanceDialogState
import org.sopt.official.feature.attendance.model.AttendanceUiState
import org.sopt.official.feature.attendance.usecase.AttendanceUiStateMapper
import javax.inject.Inject

/**
 * 리팩토링된 AttendanceViewModel
 * 
 * 개선사항:
 * 1. 단일 상태 (AttendanceUiState)로 통합 - 상태 응집성 개선
 * 2. 단일 책임 원칙 준수 - 상태 관리만 담당
 * 3. 의존성 주입으로 비즈니스 로직 분리
 * 4. 명확한 상태 플로우
 * 5. 테스트 용이성 증대
 */
@HiltViewModel
class AttendanceViewModelRefactored @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val uiStateMapper: AttendanceUiStateMapper
) : ViewModel() {
    
    // 단일 UI 상태
    private val _uiState = MutableStateFlow(AttendanceUiState.Initial)
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()
    
    // 다이얼로그 상태 (UI 상태와 별도 관리)
    private val _dialogState = MutableStateFlow<AttendanceDialogState>(AttendanceDialogState.Hidden)
    val dialogState: StateFlow<AttendanceDialogState> = _dialogState.asStateFlow()
    
    // 현재 로드된 데이터 (내부 상태)
    private var currentSoptEvent: SoptEvent? = null
    private var currentAttendanceRound: AttendanceRound? = null
    
    init {
        loadAttendanceData()
    }
    
    /**
     * 출석 데이터 로드
     */
    fun loadAttendanceData() {
        viewModelScope.launch {
            _uiState.value = uiStateMapper.mapToLoadingState()
            
            try {
                // 병렬로 데이터 로드
                val soptEventDeferred = async { attendanceRepository.fetchSoptEvent() }
                val historyDeferred = async { attendanceRepository.fetchAttendanceHistory() }
                
                val soptEventResult = soptEventDeferred.await()
                val historyResult = historyDeferred.await()
                
                // 결과 처리
                val soptEvent = soptEventResult.getOrThrow()
                val history = historyResult.getOrThrow()
                
                // SoptEvent 로드 후 AttendanceRound 로드
                currentSoptEvent = soptEvent
                loadAttendanceRound(soptEvent.id.toLong())
                
                // UI 상태 업데이트
                updateUiStateWithData(soptEvent, history, currentAttendanceRound)
                
            } catch (error: Throwable) {
                _uiState.value = uiStateMapper.mapToErrorState(error)
            }
        }
    }
    
    /**
     * 출석 라운드 정보 로드
     */
    private suspend fun loadAttendanceRound(eventId: Long) {
        try {
            val result = attendanceRepository.fetchAttendanceRound(eventId)
            currentAttendanceRound = result.getOrNull()
        } catch (error: Throwable) {
            // AttendanceRound 실패는 UI 상태에 영향 주지 않음
            currentAttendanceRound = null
        }
    }
    
    /**
     * UI 상태 업데이트
     */
    private fun updateUiStateWithData(
        soptEvent: SoptEvent,
        history: AttendanceHistory,
        attendanceRound: AttendanceRound?
    ) {
        _uiState.value = uiStateMapper.mapToSuccessState(
            soptEvent = soptEvent,
            attendanceHistory = history,
            attendanceRound = attendanceRound
        )
    }
    
    /**
     * 출석 코드 입력 다이얼로그 표시
     */
    fun showAttendanceDialog() {
        val title = currentSoptEvent?.eventName ?: "출석 확인"
        _dialogState.value = AttendanceDialogState.CodeInput(title)
    }
    
    /**
     * 다이얼로그 닫기
     */
    fun closeDialog() {
        _dialogState.value = AttendanceDialogState.Hidden
    }
    
    /**
     * 출석 코드 확인
     */
    fun checkAttendanceCode(code: String) {
        val subLectureId = currentAttendanceRound?.id
        if (subLectureId == null || subLectureId <= 0) {
            showErrorDialog("출석 정보를 불러올 수 없습니다")
            return
        }
        
        viewModelScope.launch {
            try {
                val result = attendanceRepository.confirmAttendanceCode(subLectureId, code)
                handleAttendanceCodeResult(result.getOrThrow().subLectureId)
            } catch (error: Throwable) {
                showErrorDialog(AttendanceConstants.ERROR_INVALID_CODE)
            }
        }
    }
    
    /**
     * 출석 코드 확인 결과 처리
     */
    private fun handleAttendanceCodeResult(resultCode: Long) {
        when (resultCode) {
            AttendanceConstants.ERROR_CODE -> {
                showErrorDialog(AttendanceConstants.ERROR_WRONG_CODE)
            }
            AttendanceConstants.NO_SESSION_CODE -> {
                showErrorDialog(AttendanceConstants.ERROR_BEFORE_TIME)
            }
            AttendanceConstants.TIME_RESTRICTION_CODE -> {
                showErrorDialog(AttendanceConstants.ERROR_AFTER_TIME)
            }
            else -> {
                // 성공 - 다이얼로그 닫고 데이터 새로고침
                closeDialog()
                loadAttendanceData()
            }
        }
    }
    
    /**
     * 에러 다이얼로그 표시
     */
    private fun showErrorDialog(message: String) {
        val title = currentSoptEvent?.eventName ?: "출석 확인"
        _dialogState.value = AttendanceDialogState.Error(title, message)
    }
}