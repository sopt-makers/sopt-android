package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.attendance.AttendanceHistory
import org.sopt.official.domain.entity.attendance.SoptEvent
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import org.sopt.official.feature.attendance.model.AttendanceState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {
    private var eventId: Int = 0
    private var _soptEvent = MutableStateFlow<AttendanceState<SoptEvent>>(AttendanceState.Init)
    val soptEvent: StateFlow<AttendanceState<SoptEvent>> get() = _soptEvent
    private var _attendanceHistory = MutableStateFlow<AttendanceState<AttendanceHistory>>(AttendanceState.Init)
    val attendanceHistory: StateFlow<AttendanceState<AttendanceHistory>> get() = _attendanceHistory

    private val _isFirstProgressBarActive = MutableStateFlow<Boolean>(false)
    val isFirstProgressBarActive get() = _isFirstProgressBarActive
    private val _isSecondProgressBarActive = MutableStateFlow<Boolean>(false)
    val isSecondProgressBarActive get() = _isSecondProgressBarActive
    private val _isThirdProgressBarActive = MutableStateFlow<Boolean>(false)
    val isThirdProgressBarActive get() = _isThirdProgressBarActive
    private val _isThirdProgressBarAttendance = MutableStateFlow(false)
    val isThirdProgressBarAttendance get() = _isThirdProgressBarAttendance
    private val _isThirdProgressBarBeforeAttendance = MutableStateFlow(false)
    val isThirdProgressBarBeforeAttendance get() = _isThirdProgressBarBeforeAttendance

    fun fetchSoptEvent() {
        viewModelScope.launch {
            _soptEvent.value = AttendanceState.Loading
            attendanceRepository.fetchSoptEvent()
                .onSuccess {
                    _soptEvent.value = AttendanceState.Success(it)
                    eventId = it.id
                }.onFailure {
                    Timber.e(it)
                    _soptEvent.value = AttendanceState.Failure(it)
                }
        }
    }

    fun setFirstProgressBar(isActive: Boolean) {
        _isFirstProgressBarActive.value = isActive
    }

    fun setSecondProgressBar(isActive: Boolean) {
        _isSecondProgressBarActive.value = isActive
    }

    fun setThirdProgressBar(isActive: Boolean) {
        _isThirdProgressBarActive.value = isActive
    }

    fun setThirdProgressBarAttendance(isAttendance: Boolean) {
        _isThirdProgressBarAttendance.value = isAttendance
    }

    fun setThirdProgressBarBeforeAttendance(isBeforeAttendance: Boolean) {
        _isThirdProgressBarBeforeAttendance.value = isBeforeAttendance
    }

    fun fetchAttendanceHistory() {
        viewModelScope.launch {
            _attendanceHistory.value = AttendanceState.Loading
            attendanceRepository.fetchAttendanceHistory()
                .onSuccess {
                    _attendanceHistory.value = AttendanceState.Success(it)
                }.onFailure {
                    Timber.e(it)
                    _attendanceHistory.value = AttendanceState.Failure(it)
                }
        }
    }
}