package org.sopt.official.domain.entity.attendance

data class AttendanceUserInfo(
    val generation: Int,
    val partName: String,
    val userName: String,
    val attendancePoint: Number
)
