package org.sopt.official.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAttendanceCode(
    @SerialName("subLectureId")
    val subLectureId: Long,
    @SerialName("code")
    val code: String
)