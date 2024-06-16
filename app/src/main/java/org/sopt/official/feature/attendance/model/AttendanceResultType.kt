package org.sopt.official.feature.attendance.model

enum class AttendanceResultType(val type: String) {
    ALL(type = "전체"),
    PRESENT(type = "출석"),
    LATE(type = "지각"),
    ABSENT(type = "결석");
}