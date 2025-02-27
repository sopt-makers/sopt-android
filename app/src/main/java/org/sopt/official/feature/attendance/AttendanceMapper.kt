package org.sopt.official.feature.attendance

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.domain.entity.attendance.Attendance.AttendanceDayType.HasAttendance.RoundAttendance.RoundAttendanceState
import org.sopt.official.feature.attendance.model.AttendanceDayType
import org.sopt.official.feature.attendance.model.AttendanceSession
import org.sopt.official.feature.attendance.model.AttendanceUiState.Success.AttendanceResultType
import org.sopt.official.feature.attendance.model.MidtermAttendance

fun Attendance.AttendanceDayType.toUiAttendanceDayType(): AttendanceDayType {
    return when (this) {
        is Attendance.AttendanceDayType.HasAttendance -> {
            AttendanceDayType.AttendanceDay.of(
                session,
                firstRoundAttendance,
                secondRoundAttendance
            )
        }

        is Attendance.AttendanceDayType.NoAttendance -> {
            AttendanceDayType.Event.of(session)
        }

        is Attendance.AttendanceDayType.NoSession -> {
            AttendanceDayType.None
        }
    }
}

fun Attendance.User.AttendanceCount.toTotalAttendanceResult(): ImmutableMap<AttendanceResultType, Int> {
    return persistentMapOf(
        AttendanceResultType.ALL to totalCount,
        AttendanceResultType.PRESENT to attendanceCount,
        AttendanceResultType.LATE to lateCount,
        AttendanceResultType.ABSENT to absenceCount,
    )
}

fun Attendance.AttendanceDayType.HasAttendance.RoundAttendance.toUiFirstRoundAttendance(): MidtermAttendance {
    return when (state) {
        RoundAttendanceState.ATTENDANCE -> MidtermAttendance.Present(
            attendanceAt = attendedAt.toString()
        )

        RoundAttendanceState.NOT_YET -> MidtermAttendance.NotYet(
            attendanceSession = AttendanceSession.FIRST
        )

        RoundAttendanceState.ABSENT -> MidtermAttendance.Absent
    }
}

fun Attendance.AttendanceDayType.HasAttendance.RoundAttendance.toUiSecondRoundAttendance(): MidtermAttendance {
    return when (state) {
        RoundAttendanceState.ATTENDANCE -> MidtermAttendance.Present(
            attendanceAt = attendedAt.toString()
        )

        RoundAttendanceState.NOT_YET -> MidtermAttendance.NotYet(
            attendanceSession = AttendanceSession.SECOND
        )

        RoundAttendanceState.ABSENT -> MidtermAttendance.Absent
    }
}