package org.sopt.official.domain.entity.attendance

data class AttendanceSummary(
    val normal: Int,
    val late: Int,
    val abnormal: Int,
    val participate: Int
)
