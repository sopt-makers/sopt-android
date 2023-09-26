package org.sopt.official.feature.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import org.sopt.official.core.lifecycle.combineWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val isFirstProgressBarAttendance: Boolean = false,
    val isFirstToSecondLineActive: Boolean = false,
    val isSecondProgressBarActive: Boolean = false,
    val isSecondProgressBarAttendance: Boolean = false,
    val isSecondToThirdLineActive: Boolean = false,
    val isThirdProgressBarActive: Boolean = false,
    val isThirdProgressBarAttendance: Boolean = false,
    val isThirdProgressBarTardy: Boolean = false,
    val isThirdProgressBarBeforeAttendance: Boolean = false,
    val isThirdProgressBarAbsent: Boolean = false,
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
    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title = _title.asStateFlow()
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
    var isFirstProgressBarAttendance: LiveData<Boolean> = progressBarState.map { it.isFirstProgressBarAttendance }
    val isFirstToSecondLineActive: LiveData<Boolean> = progressBarState.map { it.isFirstToSecondLineActive }
    val isSecondProgressBarActive: LiveData<Boolean> = progressBarState.map { it.isSecondProgressBarActive }
    var isSecondProgressBarAttendance: LiveData<Boolean> = progressBarState.map { it.isSecondProgressBarAttendance }
    val isSecondToThirdLineActive: LiveData<Boolean> = progressBarState.map { it.isSecondToThirdLineActive }
    val isThirdProgressBarActive: LiveData<Boolean> = progressBarState.map { it.isThirdProgressBarActive }
    val isThirdProgressBarAttendance: LiveData<Boolean> = progressBarState.map { it.isThirdProgressBarAttendance }
    val isThirdProgressBarTardy: LiveData<Boolean> = progressBarState.map { it.isThirdProgressBarTardy }
    val isThirdProgressBarBeforeAttendance: LiveData<Boolean> = progressBarState.map { it.isThirdProgressBarBeforeAttendance }
    val isThirdProgressBarVisible =
        isThirdProgressBarActive.combineWith(isThirdProgressBarTardy) { isThirdProgressBarActive, isThirdProgressBarTardy ->
            isThirdProgressBarActive == true && isThirdProgressBarTardy == true
        }
    val isThirdProgressBarActiveAndBeforeAttendance =
        isThirdProgressBarActive.combineWith(isThirdProgressBarBeforeAttendance) { isThirdProgressBarActive, isThirdProgressBarBeforeAttendance ->
            isThirdProgressBarActive == true && isThirdProgressBarBeforeAttendance == true
        }

    private val attendanceButtonState = MutableLiveData(AttendanceButtonState())
    val isAttendanceButtonEnabled: LiveData<Boolean> = attendanceButtonState.map { it.isAttendanceButtonEnabled }
    val attendanceButtonText: LiveData<String> = attendanceButtonState.map { it.attendanceButtonText }
    val isAttendanceButtonVisibility: LiveData<Boolean> = attendanceButtonState.map { it.isAttendanceButtonVisibility }

    private var subLectureId: Long = 0L
    var dialogErrorMessage: String = ""
    private var attendancesSize = 0

    init {
        fetchData()
    }

    fun fetchData() {
        fetchSoptEvent()
        fetchAttendanceHistory()
    }

    fun initDialogTitle(title: String) {
        _title.value = title
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
            // 출석 전
            0 -> {
                setFirstToSecondLine(false)
                setThirdProgressBarBeforeAttendance(true)
                setThirdProgressBar(false)
            }
            // 1차 출석 시작 ~ 2차 출석 시작 전
            1 -> {
                setThirdProgressBarBeforeAttendance(true)
                setThirdProgressBar(false)
                val firstProgressText = soptEvent.attendances[0].attendedAt
                if (firstProgressText != FIRST_ATTENDANCE_TEXT) {
                    // 1차 출석이 출석
                    setFirstProgressBar(true)
                    setFirstToSecondLine(true)
                    setFirstProgressBarAttendance(true)
                } else {
                    // 1차 출석이 결석
                    setFirstProgressBar(true)
                    setFirstToSecondLine(true)
                    setFirstProgressBarAttendance(false)
                }
            }
            // 2차 출석 시작 ~
            2 -> {
                val firstProgressText = soptEvent.attendances[0].attendedAt
                val secondProgressText = soptEvent.attendances[1].attendedAt

                if (firstProgressText != FIRST_ATTENDANCE_TEXT) {
                    // 1차 출석이 출석
                    setFirstProgressBar(true)
                    setFirstToSecondLine(true)
                    setFirstProgressBarAttendance(true)
                } else {
                    // 1차 출석이 결석
                    setFirstProgressBar(true)
                    setFirstToSecondLine(true)
                    setFirstProgressBarAttendance(false)
                }

                if (secondProgressText != SECOND_ATTENDANCE_TEXT) {
                    // 2차 출석이 출석
                    setSecondProgressBar(true)
                    setSecondToThirdLine(true)
                    setSecondProgressBarAttendance(true)
                } else {
                    // 2차 출석이 결석
                    setSecondProgressBar(true)
                    setSecondToThirdLine(true)
                    setSecondProgressBarAttendance(false)
                }

                val firstStatus = soptEvent.attendances[0].status.name
                val secondStatus = soptEvent.attendances[1].status.name
                if (firstStatus == "ATTENDANCE" && secondStatus == "ATTENDANCE") {
                    // 마지막 progress가 출석
                    setThirdProgressBar(true)
                    setThirdProgressBarAttendance(true)
                    setThirdProgressBarBeforeAttendance(true)
                    setThirdProgressBarTardy(false)
                } else if (firstStatus == "ATTENDANCE" && secondStatus == "ABSENT") {
                    // 마지막 progress가 결석
                    setThirdProgressBarBeforeAttendance(false)
                    setThirdProgressBar(true)
                    setThirdProgressBarTardy(false)
                } else if (firstStatus == "ABSENT" && secondStatus == "ATTENDANCE") {
                    // 마지막 progress가 지각
                    setThirdProgressBarBeforeAttendance(true)
                    setThirdProgressBar(true)
                    setThirdProgressBarTardy(true)
                    setThirdProgressBarAttendance(true)
                } else {
                    // 마지막 progress가 결석
                    setThirdProgressBarBeforeAttendance(false)
                    setThirdProgressBar(true)
                    setThirdProgressBarTardy(false)
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

    private fun setFirstProgressBarAttendance(isActive: Boolean) {
        setProgressBarState { copy(isFirstProgressBarAttendance = isActive) }
    }

    private fun setFirstToSecondLine(isActive: Boolean) {
        setProgressBarState { copy(isFirstToSecondLineActive = isActive) }
    }

    private fun setSecondProgressBar(isActive: Boolean) {
        setProgressBarState { copy(isSecondProgressBarActive = isActive) }
    }

    private fun setSecondProgressBarAttendance(isActive: Boolean) {
        setProgressBarState { copy(isSecondProgressBarAttendance = isActive) }
    }

    private fun setSecondToThirdLine(isActive: Boolean) {
        setProgressBarState { copy(isSecondToThirdLineActive = isActive) }
    }

    private fun setThirdProgressBar(isActive: Boolean) {
        setProgressBarState { copy(isThirdProgressBarActive = isActive) }
    }

    private fun setThirdProgressBarAttendance(isAttendance: Boolean) {
        setProgressBarState { copy(isThirdProgressBarAttendance = isAttendance) }
    }

    private fun setThirdProgressBarTardy(isTardy: Boolean) {
        setProgressBarState { copy(isThirdProgressBarTardy = isTardy) }
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
                        -2L -> showDialog("코드가 일치하지 않아요!")
                        -1L -> showDialog("출석 시간 전입니다.")
                        0L -> showDialog("출석이 이미 종료되었습니다.")
                        else -> _dialogState.value = DialogState.Close
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

    companion object {
        private const val FIRST_ATTENDANCE_TEXT = "1차 출석"
        private const val SECOND_ATTENDANCE_TEXT = "2차 출석"
    }
}
