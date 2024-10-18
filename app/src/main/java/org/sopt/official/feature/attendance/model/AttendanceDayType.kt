package org.sopt.official.feature.attendance.model

sealed interface AttendanceDayType {
    /** 출석이 진행되는 날 **/
    data class AttendanceDay(
        val eventDate: String,
        val eventLocation: String,
        val eventName: String,
        val firstAttendance: MidtermAttendance,
        val secondAttendance: MidtermAttendance,
        val finalAttendance: FinalAttendance,
    ) : AttendanceDayType

    /** 출석할 필요가 없는 날 **/
    data class Event(
        val eventDate: String,
        val eventLocation: String,
        val eventName: String,
    ) : AttendanceDayType

    /** 아무 일정이 없는 날 **/
    data object NONE : AttendanceDayType
}