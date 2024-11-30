package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import org.sopt.official.feature.attendance.model.AttendanceUiState
import javax.inject.Inject

@HiltViewModel
class NewAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
) : ViewModel() {

    init {
        fetchData()
    }

    private val _uiState: MutableStateFlow<AttendanceUiState> =
        MutableStateFlow(AttendanceUiState.Loading)
    val uiState: StateFlow<AttendanceUiState> = _uiState


    fun fetchData() {
        fetchSoptEvent()
        fetchAttendanceHistory()
    }

    private fun fetchSoptEvent() {
        // TODO
    }

    private fun fetchAttendanceHistory() {
        // TODO
    }
}
