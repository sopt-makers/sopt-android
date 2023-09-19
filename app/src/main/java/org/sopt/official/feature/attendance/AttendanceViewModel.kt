package org.sopt.official.feature.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
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
import org.sopt.official.feature.attendance.model.DialogState
import timber.log.Timber
import javax.inject.Inject

data class ProgressBarState(
    val isFirstProgressBarActive: Boolean = false,
    val isSecondProgressBarActive: Boolean = false,
    val isThirdProgressBarActive: Boolean = false,
    val isThirdProgressBarAttendance: Boolean = false,
    val isThirdProgressBarBeforeAttendance: Boolean = false
)

data class AttendanceButtonState(
    val isAttendanceButtonEnabled: Boolean = false,
    val attendanceButtonText: String = "",
    val isAttendanceButtonVisibility: Boolean = false
)

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
    private var _dialogState = MutableStateFlow<DialogState>(DialogState.Show)
    val dialogState: StateFlow<DialogState> get() = _dialogState

    private val progressBarState = MutableLiveData(ProgressBarState())
    val isFirstProgressBarActive: LiveData<Boolean> = progressBarState.map { it.isFirstProgressBarActive }
    val isSecondProgressBarActive: LiveData<Boolean> = progressBarState.map { it.isSecondProgressBarActive }
    val isThirdProgressBarActive: LiveData<Boolean> = progressBarState.map { it.isThirdProgressBarActive }
    val isThirdProgressBarAttendance: LiveData<Boolean> = progressBarState.map { it.isThirdProgressBarAttendance }
    val isThirdProgressBarBeforeAttendance: LiveData<Boolean> = progressBarState.map { it.isThirdProgressBarBeforeAttendance }

    private val attendanceButtonState = MutableLiveData(AttendanceButtonState())
    val isAttendanceButtonEnabled: LiveData<Boolean> = attendanceButtonState.map { it.isAttendanceButtonEnabled }
    val attendanceButtonText: LiveData<String> = attendanceButtonState.map { it.attendanceButtonText }
    val isAttendanceButtonVisibility: LiveData<Boolean> = attendanceButtonState.map { it.isAttendanceButtonVisibility }

    private var subLectureId: Long = 0L
    var dialogErrorMessage: String = ""
    private var attendancesSize = 0

    companion object {
        private const val FIRST_ATTENDANCE_TEXT = "1차 출석"
        private const val SECOND_ATTENDANCE_TEXT = "2차 출석"
    }

    fun fetchSoptEvent() {
        viewModelScope.launch {
            _soptEvent.value = AttendanceState.Loading
            attendanceRepository.fetchSoptEvent()
                .onSuccess {
                    _soptEvent.value = AttendanceState.Success(it)
                    eventId = it.id
                    attendancesSize = it.attendances.size
                    fetchAttendanceRound()
                }.onFailure {
                    Timber.e(it)
                    _soptEvent.value = AttendanceState.Failure(it)
                }
        }
    }

    fun setProgressBar(soptEvent: SoptEvent) {
        when (soptEvent.attendances.size) {
            0 -> {
                setThirdProgressBarBeforeAttendance(true) // 출석 전
                setThirdProgressBar(false)
            }
            1 -> {
                setThirdProgressBarBeforeAttendance(true)
                setThirdProgressBar(false)
                val firstProgressText = soptEvent.attendances[0].attendedAt
                if (firstProgressText != FIRST_ATTENDANCE_TEXT) {
                    setFirstProgressBar(true)
                } else {
                    setFirstProgressBar(false)
                }
            }
            2 -> {
                val firstProgressText = soptEvent.attendances[0].attendedAt
                val secondProgressText = soptEvent.attendances[1].attendedAt

                if (firstProgressText != FIRST_ATTENDANCE_TEXT) {
                    setFirstProgressBar(true)
                } else {
                    setFirstProgressBar(false)
                }

                if (secondProgressText != SECOND_ATTENDANCE_TEXT) {
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
                    // 결석 상태
                    setThirdProgressBarBeforeAttendance(false)
                    setThirdProgressBar(false)
                } else if (firstStatus == "ABSENT" && secondStatus == "ATTENDANCE") {
                    setThirdProgressBar(true)
                    setThirdProgressBarAttendance(false)
                } else {
                    // 결석 상태
                    setThirdProgressBarBeforeAttendance(false)
                    setThirdProgressBar(false)
                }
            }
        }
    }

    private fun setProgressBarState(block: ProgressBarState.() -> ProgressBarState) {
        progressBarState.value = progressBarState.value?.block()
    }

    private fun setAttendanceButtonState(block: AttendanceButtonState.() -> AttendanceButtonState) {
        attendanceButtonState.value = attendanceButtonState.value?.block()
    }

    private fun setFirstProgressBar(isActive: Boolean) {
        setProgressBarState { copy(isFirstProgressBarActive = isActive) }
    }

    private fun setSecondProgressBar(isActive: Boolean) {
        setProgressBarState { copy(isSecondProgressBarActive = isActive) }
    }

    private fun setThirdProgressBar(isActive: Boolean) {
        setProgressBarState { copy(isThirdProgressBarActive = isActive) }
    }

    private fun setThirdProgressBarAttendance(isAttendance: Boolean) {
        setProgressBarState { copy(isThirdProgressBarAttendance = isAttendance) }
    }

    private fun setThirdProgressBarBeforeAttendance(isBeforeAttendance: Boolean) {
        setProgressBarState { copy(isThirdProgressBarBeforeAttendance = isBeforeAttendance) }
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
                Timber.tag("zzzz id").i(it.id.toString())
                when (it.id) {
                    -1L -> {
                        setAttendanceButtonVisibility(false)
                    }
                    0L -> {
                        setAttendanceButtonText(it.roundText)
                        setAttendanceButtonVisibility(true)
                        setAttendanceButtonEnabled(false)
                    }
                    else -> {
                        if (it.roundText.isNotEmpty() && attendancesSize == it.roundText[0].code - '0'.code) {
                            setAttendanceButtonText(it.roundText.substring(0, 5) + " 종료")
                            setAttendanceButtonVisibility(true)
                            setAttendanceButtonEnabled(false)
                        } else {
                            setAttendanceButtonText(it.roundText)
                            setAttendanceButtonVisibility(true)
                            setAttendanceButtonEnabled(true)
                        }
                    }
                }
            }.onFailure {
                Timber.tag("zzzz failure").e(it)
                Timber.e(it)
            }
    }

    private fun setAttendanceButtonVisibility(isVisibility: Boolean) {
        setAttendanceButtonState { copy(isAttendanceButtonVisibility = isVisibility) }
    }

    private fun setAttendanceButtonEnabled(isEnabled: Boolean) {
        setAttendanceButtonState { copy(isAttendanceButtonEnabled = isEnabled) }
    }

    private fun setAttendanceButtonText(text: String) {
        setAttendanceButtonState { copy(attendanceButtonText = text) }
    }

    fun checkAttendanceCode(code: String) {
        _dialogState.value = DialogState.Show
        viewModelScope.launch {
            attendanceRepository.confirmAttendanceCode(subLectureId, code)
                .onSuccess {
                    when (it.subLectureId) {
                        -2L -> {
                            showDialog("코드가 일치하지 않아요!")
                        }
                        -1L -> {
                            showDialog("출석 시간 전입니다.")
                        }
                        0L -> {
                            showDialog("출석이 이미 종료되었습니다.")
                        }
                        else -> {
                            _dialogState.value = DialogState.Close
                        }
                    }
                }.onFailure {
                    Timber.e(it)
                }
        }
    }

    fun initDialogState() {
        _dialogState.value = DialogState.Show
    }

    private fun showDialog(message: String) {
        dialogErrorMessage = message
        _dialogState.value = DialogState.Failure
    }
}
