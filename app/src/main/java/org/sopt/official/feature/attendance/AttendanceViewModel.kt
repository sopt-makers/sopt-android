package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.attendance.AttendanceHistory
import org.sopt.official.domain.entity.attendance.AttendanceRound
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
    private var _attendanceRound = MutableStateFlow<AttendanceState<AttendanceRound>>(AttendanceState.Init)
    val attendanceRound: StateFlow<AttendanceState<AttendanceRound>> get() = _attendanceRound

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

    private val _isAttendanceButtonEnabled = MutableStateFlow(false)
    val isAttendanceButtonEnabled get() = _isAttendanceButtonEnabled
    private val _attendanceButtonText = MutableStateFlow("")
    val attendanceButtonText get() = _attendanceButtonText
    private val _isAttendanceButtonVisibility = MutableStateFlow(false)
    val isAttendanceButtonVisibility get() = _isAttendanceButtonVisibility
    private var subLectureId: Long = 0L

    fun fetchSoptEvent() {
        viewModelScope.launch {
            _soptEvent.value = AttendanceState.Loading
            attendanceRepository.fetchSoptEvent()
                .onSuccess {
                    _soptEvent.value = AttendanceState.Success(it)
                    eventId = it.id
                    setProgressBar(it)
                    fetchAttendanceRound()
                }.onFailure {
                    Timber.e(it)
                    _soptEvent.value = AttendanceState.Failure(it)
                }
        }
    }

    private fun setProgressBar(soptEvent: SoptEvent) {
        val firstProgressText = soptEvent.attendances[0].attendedAt
        val secondProgressText = soptEvent.attendances[1].attendedAt

        if (firstProgressText != "1차 출석") {
            setFirstProgressBar(true)
        } else {
            setFirstProgressBar(false)
        }

        if (secondProgressText != "2차 출석") {
            setSecondProgressBar(true)
        } else {
            setSecondProgressBar(false)
        }

        val firstStatus = soptEvent.attendances[0].status.name
        val secondStatus = soptEvent.attendances[1].status.name
        if (firstStatus == "ATTENDANCE" && secondStatus == "ATTENDANCE") {
            setThirdProgressBar(true)
            setThirdProgressBarAttendance(true)
        } else if (firstStatus == "ATTENDANCE" && secondStatus == "ABSENT") {
            setThirdProgressBarBeforeAttendance(false)
            setThirdProgressBar(false)
        } else if (firstStatus == "ABSENT" && secondStatus == "ATTENDANCE") {
            setThirdProgressBar(true)
            setThirdProgressBarAttendance(false)
        } else {
            setThirdProgressBarBeforeAttendance(true)
            setThirdProgressBar(false)
        }
    }

    private fun setFirstProgressBar(isActive: Boolean) {
        _isFirstProgressBarActive.value = isActive
    }

    private fun setSecondProgressBar(isActive: Boolean) {
        _isSecondProgressBarActive.value = isActive
    }

    private fun setThirdProgressBar(isActive: Boolean) {
        _isThirdProgressBarActive.value = isActive
    }

    private fun setThirdProgressBarAttendance(isAttendance: Boolean) {
        _isThirdProgressBarAttendance.value = isAttendance
    }

    private fun setThirdProgressBarBeforeAttendance(isBeforeAttendance: Boolean) {
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

    private suspend fun fetchAttendanceRound() {
        attendanceRepository.fetchAttendanceRound(eventId.toLong())
            .onSuccess {
                _attendanceRound.value = AttendanceState.Success(it)
                subLectureId = it.id
                when (it.id) {
                    -1L -> setAttendanceButtonVisibility(false)
                    0L -> {
                        setAttendanceButtonText(it.roundText)
                        setAttendanceButtonVisibility(true)
                        setAttendanceButtonEnabled(false)
                    }
                    else -> {
                        setAttendanceButtonText(it.roundText)
                        setAttendanceButtonVisibility(true)
                        setAttendanceButtonEnabled(true)
                    }
                }
            }.onFailure {
                Timber.e(it)
            }
    }

    private fun setAttendanceButtonVisibility(isVisibility: Boolean) {
        isAttendanceButtonVisibility.value = isVisibility
    }
    private fun setAttendanceButtonEnabled(isEnabled: Boolean) {
        isAttendanceButtonEnabled.value = isEnabled
    }

    private fun setAttendanceButtonText(text: String) {
        attendanceButtonText.value = text
    }
}