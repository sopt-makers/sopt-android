package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.domain.entity.attendance.FetchAttendanceCurrentRoundResult
import org.sopt.official.domain.repository.attendance.NewAttendanceRepository
import org.sopt.official.feature.attendance.model.AttendanceSession
import org.sopt.official.feature.attendance.model.AttendanceUiState
import javax.inject.Inject

@HiltViewModel
class NewAttendanceViewModel @Inject constructor(
    private val attendanceRepository: NewAttendanceRepository,
) : ViewModel() {

    init {
        fetchAttendanceInfo()
    }

    private val _uiState: MutableStateFlow<AttendanceUiState> = MutableStateFlow(AttendanceUiState.Loading)
    val uiState: StateFlow<AttendanceUiState> = _uiState

    private val _errorMessage: Channel<String> = Channel(capacity = Channel.BUFFERED)
    val errorMessage: Flow<String> = _errorMessage.receiveAsFlow()

    fun fetchAttendanceInfo() {
        viewModelScope.launch {
            val attendance: Attendance = attendanceRepository.fetchAttendanceInfo()
            _uiState.update {
                AttendanceUiState.Success.of(attendance)
            }
        }
    }

    fun fetchCurrentRound() {
        val lectureId: Long? = (uiState.value as AttendanceUiState.Success).lectureId
        viewModelScope.launch {
            if (lectureId == null) {
                _errorMessage.send("서버로부터 출석 세션 정보를 받아오지 못했습니다.")
                return@launch
            }

            when (val result: FetchAttendanceCurrentRoundResult = attendanceRepository.fetchAttendanceCurrentRound(lectureId)) {
                is FetchAttendanceCurrentRoundResult.Success -> _uiState.update {
                    (it as AttendanceUiState.Success).copy(
                        attendanceSession = if (result.round == null) null
                        else AttendanceSession.of(result.round)
                    )
                }

                is FetchAttendanceCurrentRoundResult.Failure -> _errorMessage.send("서버로부터 세션 차수 정보를 받아오지 못했습니다.")
            }
        }
    }

    fun clearAttendanceCode() {
        _uiState.update {
            (_uiState.value as AttendanceUiState.Success).copy(
                attendanceSession = null,
                codes = emptyList(),
                isCodeCorrect = true
            )
        }
    }
}
