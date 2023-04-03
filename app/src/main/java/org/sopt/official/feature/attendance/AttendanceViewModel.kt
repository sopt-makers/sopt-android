package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.attendance.AttendanceLog
import org.sopt.official.domain.entity.attendance.AttendanceSummary
import org.sopt.official.domain.entity.attendance.AttendanceUserInfo
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
    private var _attendanceUserInfo = MutableStateFlow<AttendanceState<AttendanceUserInfo>>(AttendanceState.Init)
    val attendanceUserInfo: StateFlow<AttendanceState<AttendanceUserInfo>> get() = _attendanceUserInfo
    private var _attendanceSummary = MutableStateFlow<AttendanceState<AttendanceSummary>>(AttendanceState.Init)
    val attendanceSummary: StateFlow<AttendanceState<AttendanceSummary>> get() = _attendanceSummary
    private var _attendanceLog = MutableStateFlow<AttendanceState<List<AttendanceLog>>>(AttendanceState.Init)
    val attendanceLog: StateFlow<AttendanceState<List<AttendanceLog>>> get() = _attendanceLog

    fun fetchSoptEvent() {
        viewModelScope.launch {
            _soptEvent.value = AttendanceState.Loading
            attendanceRepository.fetchSoptEvent()
                .onSuccess { _soptEvent.value = AttendanceState.Success(it) }
                .onFailure { _soptEvent.value = AttendanceState.Failure(it) }
        }
    }

    fun fetchAttendanceHistory() {
        viewModelScope.launch {
            _attendanceUserInfo.value = AttendanceState.Loading
            _attendanceSummary.value = AttendanceState.Loading
            _attendanceLog.value = AttendanceState.Loading
            attendanceRepository.fetchAttendanceHistory()
                .onSuccess {
                    _attendanceUserInfo.value = AttendanceState.Success(it.userInfo)
                    _attendanceSummary.value = AttendanceState.Success(it.attendanceSummary)
                    _attendanceLog.value = AttendanceState.Success(it.attendanceLog)
                }.onFailure {
                    _attendanceUserInfo.value = AttendanceState.Failure(it)
                    _attendanceSummary.value = AttendanceState.Failure(it)
                    _attendanceLog.value = AttendanceState.Failure(it)
                }
        }
    }
}