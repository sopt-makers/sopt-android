package org.sopt.official.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.domain.entity.attendance.Attendance.AttendanceDayType.HasAttendance.RoundAttendance.RoundAttendanceState
import org.sopt.official.domain.repository.attendance.NewAttendanceRepository
import org.sopt.official.feature.attendance.model.AttendanceUiState
import org.sopt.official.feature.attendance.model.AttendanceUiState.Success.AttendanceDayType
import org.sopt.official.feature.attendance.model.AttendanceUiState.Success.AttendanceDayType.AttendanceDay.MidtermAttendance
import org.sopt.official.feature.attendance.model.AttendanceUiState.Success.AttendanceDayType.AttendanceDay.MidtermAttendance.NotYet.AttendanceSession
import org.sopt.official.feature.attendance.model.AttendanceUiState.Success.AttendanceHistory
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
                AttendanceUiState.Success(
                    attendanceDayType = when (attendance.attendanceDayType) {
                        is Attendance.AttendanceDayType.HasAttendance -> {
                            val sessionInfo = attendance.attendanceDayType.sessionInfo
                            val firstRoundAttendance = attendance.attendanceDayType.firstRoundAttendance
                            val secondRoundAttendance = attendance.attendanceDayType.secondRoundAttendance

                            AttendanceDayType.AttendanceDay(
                                eventDate = "${sessionInfo.sessionStartTime} - ${sessionInfo.sessionEndTime}",
                                eventLocation = sessionInfo.location ?: "장소 정보를 불러올 수 없습니다.",
                                eventName = sessionInfo.sessionName,
                                firstAttendance = when (firstRoundAttendance.state) {
                                    RoundAttendanceState.ATTENDANCE -> MidtermAttendance.Present(
                                        attendanceAt = firstRoundAttendance.attendedAt.toString()
                                    )

                                    RoundAttendanceState.NOT_YET -> MidtermAttendance.NotYet(
                                        attendanceSession = AttendanceSession.FIRST
                                    )

                                    RoundAttendanceState.ABSENT -> MidtermAttendance.Absent
                                },
                                secondAttendance = when (secondRoundAttendance.state) {
                                    RoundAttendanceState.ATTENDANCE -> MidtermAttendance.Present(
                                        attendanceAt = secondRoundAttendance.attendedAt.toString()
                                    )


                                    RoundAttendanceState.NOT_YET -> MidtermAttendance.NotYet(
                                        attendanceSession = AttendanceSession.FIRST
                                    )

                                    RoundAttendanceState.ABSENT -> MidtermAttendance.Absent
                                },
                                finalAttendance = run {
                                    if (secondRoundAttendance.state == RoundAttendanceState.NOT_YET) AttendanceDayType.AttendanceDay.FinalAttendance.NOT_YET
                                    if (firstRoundAttendance.state == RoundAttendanceState.ABSENT && secondRoundAttendance.state == RoundAttendanceState.ABSENT) AttendanceDayType.AttendanceDay.FinalAttendance.ABSENT
                                    if (firstRoundAttendance.state == RoundAttendanceState.ABSENT || secondRoundAttendance.state == RoundAttendanceState.ABSENT) AttendanceDayType.AttendanceDay.FinalAttendance.LATE
                                    else AttendanceDayType.AttendanceDay.FinalAttendance.PRESENT
                                }
                            )
                        }

                        is Attendance.AttendanceDayType.NoAttendance -> {
                            val sessionInfo = attendance.attendanceDayType.sessionInfo
                            AttendanceDayType.Event(
                                eventDate = "${sessionInfo.sessionStartTime} - ${sessionInfo.sessionEndTime}",
                                eventLocation = sessionInfo.location ?: "장소 정보를 불러올 수 없습니다.",
                                eventName = sessionInfo.sessionName
                            )
                        }

                        is Attendance.AttendanceDayType.NoSession -> {
                            AttendanceDayType.None
                        }
                    },
                    userTitle = "${attendance.user.generation}기 ${attendance.user.part.partName}파트 ${attendance.user.name}",
                    attendanceScore = attendance.user.attendanceScore.toFloat(),
                    totalAttendanceResult = persistentMapOf(
                        AttendanceUiState.Success.AttendanceResultType.ALL to attendance.user.attendanceCount.totalCount,
                        AttendanceUiState.Success.AttendanceResultType.PRESENT to attendance.user.attendanceCount.attendanceCount,
                        AttendanceUiState.Success.AttendanceResultType.LATE to attendance.user.attendanceCount.lateCount,
                        AttendanceUiState.Success.AttendanceResultType.ABSENT to attendance.user.attendanceCount.absenceCount,
                    ),
                    attendanceHistoryList = attendance.user.attendanceHistory.map { attendanceLog: Attendance.User.AttendanceLog ->
                        AttendanceHistory(
                            status = when (attendanceLog.attendanceState) {
                                Attendance.User.AttendanceLog.AttendanceState.PARTICIPATE -> "참여"
                                Attendance.User.AttendanceLog.AttendanceState.ATTENDANCE -> "출석"
                                Attendance.User.AttendanceLog.AttendanceState.TARDY -> "지각"
                                Attendance.User.AttendanceLog.AttendanceState.ABSENT -> "결석"
                            },
                            eventName = attendanceLog.sessionName,
                            date = attendanceLog.date
                        )
                    }.toPersistentList()
                )
            }
        }
    }

}
