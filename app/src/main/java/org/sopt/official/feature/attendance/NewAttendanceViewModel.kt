package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import org.sopt.official.feature.attendance.model.AttendanceUiState
import javax.inject.Inject

@HiltViewModel
class NewAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    init {
        fetchData()
    }

    private val _uiState: MutableStateFlow<AttendanceUiState> =
        MutableStateFlow(AttendanceUiState.Loading)
    val uiState: StateFlow<AttendanceUiState> = _uiState

    private var fakeTitle: String = ""

    fun updateUiState() {
        viewModelScope.launch {
            _uiState.emit(AttendanceUiState.Success(fakeTitle))
        }
    }

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