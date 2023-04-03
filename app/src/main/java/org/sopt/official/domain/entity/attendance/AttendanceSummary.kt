package org.sopt.official.domain.entity.attendance

data class AttendanceSummary(
    val all: Int,
    val normal: Int,
    val late: Int,
    val abnormal: Int
)
