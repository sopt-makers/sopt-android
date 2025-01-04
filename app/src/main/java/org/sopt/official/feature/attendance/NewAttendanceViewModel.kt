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
import org.sopt.official.domain.entity.attendance.AttendanceInfo
import org.sopt.official.domain.entity.attendance.AttendanceInfo.AttendanceDayType.HasAttendance.RoundAttendance.RoundAttendanceState
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
            val attendanceInfo: AttendanceInfo = attendanceRepository.fetchAttendanceInfo()
            _uiState.update {
                AttendanceUiState.Success(
                    attendanceDayType = when (attendanceInfo.attendanceDayType) {
                        is AttendanceInfo.AttendanceDayType.HasAttendance -> {
                            val sessionInfo = attendanceInfo.attendanceDayType.sessionInfo
                            val firstRoundAttendance = attendanceInfo.attendanceDayType.firstRoundAttendance
                            val secondRoundAttendance = attendanceInfo.attendanceDayType.secondRoundAttendance

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

                        is AttendanceInfo.AttendanceDayType.NoAttendance -> {
                            val sessionInfo = attendanceInfo.attendanceDayType.sessionInfo
                            AttendanceDayType.Event(
                                eventDate = "${sessionInfo.sessionStartTime} - ${sessionInfo.sessionEndTime}",
                                eventLocation = sessionInfo.location ?: "장소 정보를 불러올 수 없습니다.",
                                eventName = sessionInfo.sessionName
                            )
                        }

                        is AttendanceInfo.AttendanceDayType.NoSession -> {
                            AttendanceDayType.None
                        }
                    },
                    userTitle = "${attendanceInfo.user.generation}기 ${attendanceInfo.user.part.partName}파트 ${attendanceInfo.user.name}",
                    attendanceScore = attendanceInfo.user.attendanceScore.toFloat(),
                    totalAttendanceResult = persistentMapOf(
                        AttendanceUiState.Success.AttendanceResultType.ALL to attendanceInfo.user.attendanceCount.totalCount,
                        AttendanceUiState.Success.AttendanceResultType.PRESENT to attendanceInfo.user.attendanceCount.attendanceCount,
                        AttendanceUiState.Success.AttendanceResultType.LATE to attendanceInfo.user.attendanceCount.lateCount,
                        AttendanceUiState.Success.AttendanceResultType.ABSENT to attendanceInfo.user.attendanceCount.absenceCount,
                    ),
                    attendanceHistoryList = attendanceInfo.user.attendanceHistory.map { attendanceLog: AttendanceInfo.User.AttendanceLog ->
                        AttendanceHistory(
                            status = when (attendanceLog.attendanceState) {
                                AttendanceInfo.User.AttendanceLog.AttendanceState.PARTICIPATE -> "참여"
                                AttendanceInfo.User.AttendanceLog.AttendanceState.ATTENDANCE -> "출석"
                                AttendanceInfo.User.AttendanceLog.AttendanceState.TARDY -> "지각"
                                AttendanceInfo.User.AttendanceLog.AttendanceState.ABSENT -> "결석"
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
