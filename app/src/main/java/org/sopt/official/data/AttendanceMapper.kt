package org.sopt.official.data

import org.sopt.official.data.model.attendance.AttendanceHistoryResponse
import org.sopt.official.data.model.attendance.AttendanceHistoryResponse.AttendanceResponse
import org.sopt.official.data.model.attendance.SoptEventResponse
import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.domain.entity.attendance.Attendance.AttendanceDayType
import org.sopt.official.domain.entity.attendance.Attendance.AttendanceDayType.HasAttendance.RoundAttendance
import org.sopt.official.domain.entity.attendance.Attendance.AttendanceDayType.HasAttendance.RoundAttendance.RoundAttendanceState
import org.sopt.official.domain.entity.attendance.Attendance.Session
import org.sopt.official.domain.entity.attendance.Attendance.User.AttendanceLog.AttendanceState
import java.time.LocalDateTime

fun mapToAttendance(
    attendanceHistoryResponse: AttendanceHistoryResponse?,
    soptEventResponse: SoptEventResponse?
): Attendance {
    return Attendance(
        sessionId = soptEventResponse?.id ?: Attendance.UNKNOWN_SESSION_ID,
        user = Attendance.User(
            name = attendanceHistoryResponse?.name ?: Attendance.User.UNKNOWN_NAME,
            generation = attendanceHistoryResponse?.generation ?: Attendance.User.UNKNOWN_GENERATION,
            part = Attendance.User.Part.valueOf(attendanceHistoryResponse?.part ?: Attendance.User.UNKNOWN_PART),
            attendanceScore = attendanceHistoryResponse?.score ?: 0.0,
            attendanceCount = Attendance.User.AttendanceCount(
                attendanceCount = attendanceHistoryResponse?.attendanceCount?.normal ?: 0,
                lateCount = attendanceHistoryResponse?.attendanceCount?.late ?: 0,
                absenceCount = attendanceHistoryResponse?.attendanceCount?.abnormal ?: 0,
            ),
            attendanceHistory = attendanceHistoryResponse?.attendances?.map { attendanceResponse: AttendanceResponse ->
                Attendance.User.AttendanceLog(
                    sessionName = attendanceResponse.eventName,
                    date = attendanceResponse.date,
                    attendanceState = AttendanceState.valueOf(attendanceResponse.attendanceState)
                )
            } ?: emptyList(),
        ),
        attendanceDayType = mapToAttendanceDayType(soptEventResponse)
    )
}

private fun mapToAttendanceDayType(soptEventResponse: SoptEventResponse?): AttendanceDayType {
    return when (soptEventResponse?.type) {
        "HAS_ATTENDANCE" -> {
            val firstAttendanceResponse: SoptEventResponse.AttendanceResponse? = soptEventResponse.attendances.getOrNull(0)
            val secondAttendanceResponse: SoptEventResponse.AttendanceResponse? = soptEventResponse.attendances.getOrNull(1)
            AttendanceDayType.HasAttendance(
                session = Session(
                    name = soptEventResponse.eventName,
                    location = soptEventResponse.location.ifBlank { null },
                    startAt = LocalDateTime.parse(soptEventResponse.startAt),
                    endAt = LocalDateTime.parse(soptEventResponse.endAt),
                ),
                firstRoundAttendance = RoundAttendance(
                    state = if (firstAttendanceResponse == null) RoundAttendanceState.NOT_YET else RoundAttendanceState.valueOf(
                        firstAttendanceResponse.status
                    ),
                    attendedAt = LocalDateTime.parse(firstAttendanceResponse?.attendedAt),
                ),
                secondRoundAttendance = RoundAttendance(
                    state = if (secondAttendanceResponse == null) RoundAttendanceState.NOT_YET else RoundAttendanceState.valueOf(
                        secondAttendanceResponse.status
                    ),
                    attendedAt = LocalDateTime.parse(secondAttendanceResponse?.attendedAt),
                ),
            )
        }

        "NO_ATTENDANCE" -> {
            AttendanceDayType.NoAttendance(
                session = Session(
                    name = soptEventResponse.eventName,
                    location = soptEventResponse.location.ifBlank { null },
                    startAt = LocalDateTime.parse(soptEventResponse.startAt),
                    endAt = LocalDateTime.parse(soptEventResponse.endAt),
                )
            )
        }

        "NO_SESSION" -> {
            AttendanceDayType.NoSession
        }

        else -> {
            AttendanceDayType.NoSession
        }
    }
}