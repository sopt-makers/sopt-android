package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.attendance.SoptEvent
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import org.sopt.official.feature.attendance.model.AttendanceState
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {
    private var _soptEvent = MutableStateFlow<AttendanceState<SoptEvent>>(AttendanceState.Init)
    val soptEvent: StateFlow<AttendanceState<SoptEvent>> get() = _soptEvent

    fun fetchSoptEvent() {
        viewModelScope.launch {
            _soptEvent.value = AttendanceState.Loading
            attendanceRepository.fetchSoptEvent()
                .onSuccess { _soptEvent.value = AttendanceState.Success(it) }
                .onFailure { _soptEvent.value = AttendanceState.Failure(it) }
        }
    }
}