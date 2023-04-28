package org.sopt.official.domain.entity.attendance

data class AttendanceRound(
    val id: Long,
    val roundText: String,
) {
    companion object {
        val ERROR = AttendanceRound(-2, "")
    }
}
