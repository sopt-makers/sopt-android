package org.sopt.official.domain.entity.attendance

enum class AttendanceButtonType(val attendanceRound: AttendanceRound) {
    GONE_BUTTON(AttendanceRound(-1, "")),
    BEFORE_FIRST_ATTENDANCE(AttendanceRound(0, "1차 출석 전")),
    BEFORE_SECOND_ATTENDANCE(AttendanceRound(0, "2차 출석 전")),
    AFTER_SECOND_ATTENDANCE(AttendanceRound(0, "2차 출석 종료")),
    ERROR(AttendanceRound(-2, ""))
}