package org.sopt.official.feature.attendance.model

import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.feature.attendance.toUiFirstRoundAttendance
import org.sopt.official.feature.attendance.toUiSecondRoundAttendance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

sealed interface AttendanceDayType {
    /** 출석이 진행되는 날 **/
    data class AttendanceDay(
        val eventDate: String,
        val eventLocation: String,
        val eventName: String,
        val firstRoundAttendance: MidtermAttendance,
        val secondRoundAttendance: MidtermAttendance,
    ) : AttendanceDayType {
        val finalAttendance: FinalAttendance =
            FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance)

        companion object {
            fun of(
                session: Attendance.Session,
                firstRoundAttendance: Attendance.AttendanceDayType.HasAttendance.RoundAttendance,
                secondRoundAttendance: Attendance.AttendanceDayType.HasAttendance.RoundAttendance
            ): AttendanceDay {
                return AttendanceDay(
                    eventDate = formatSessionTime(session.startAt, session.endAt),
                    eventLocation = session.location ?: "장소 정보를 불러올 수 없습니다.",
                    eventName = session.name,
                    firstRoundAttendance = firstRoundAttendance.toUiFirstRoundAttendance(),
                    secondRoundAttendance = secondRoundAttendance.toUiSecondRoundAttendance(),
                )
            }
        }
    }

    /** 출석할 필요가 없는 날 **/
    data class Event(
        val eventDate: String,
        val eventLocation: String,
        val eventName: String,
    ) : AttendanceDayType {
        companion object {
            fun of(session: Attendance.Session): Event {
                return Event(
                    eventDate = formatSessionTime(session.startAt, session.endAt),
                    eventLocation = session.location ?: "장소 정보를 불러올 수 없습니다.",
                    eventName = session.name
                )
            }
        }
    }

    /** 아무 일정이 없는 날 **/
    data object None : AttendanceDayType
}

private fun formatSessionTime(startAt: LocalDateTime, endAt: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("M월 d일")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    return "${startAt.format(dateFormatter)} ${
        startAt.dayOfWeek.getDisplayName(
            TextStyle.FULL, Locale.KOREAN
        )
    } ${
        startAt.format(
            timeFormatter
        )
    } - " + if (startAt.toLocalDate() == endAt.toLocalDate()) endAt.format(timeFormatter)
    else "${endAt.format(dateFormatter)} ${
        endAt.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
    } ${endAt.format(timeFormatter)}"
}