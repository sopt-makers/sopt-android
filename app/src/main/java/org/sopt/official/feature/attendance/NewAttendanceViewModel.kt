package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.domain.repository.attendance.NewAttendanceRepository
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

    fun fetchAttendanceInfo() {
        viewModelScope.launch {
            val attendance: Attendance = attendanceRepository.fetchAttendanceInfo()
            _uiState.update {
                AttendanceUiState.Success.of(attendance)
            }
        }
    }
}
