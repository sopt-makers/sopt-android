package org.sopt.official.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.AttendanceRound

@Serializable
data class AttendanceRoundResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("round")
    val round: Int
) {
    fun toEntity(): AttendanceRound = AttendanceRound(
        this.id,
        "${round}차 출석 인증하기"
    )
}