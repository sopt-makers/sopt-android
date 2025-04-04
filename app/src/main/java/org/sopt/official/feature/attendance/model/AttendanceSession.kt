package org.sopt.official.feature.attendance.model

enum class AttendanceSession(val type: String) {
    FIRST("1차 출석"),
    SECOND("2차 출석");

    companion object {
        fun of(round: Int): AttendanceSession {
            return entries[round - 1]
        }
    }
}