package org.sopt.official.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceCodeResponse(
    @SerialName("subLectureId")
    val subLectureId: Long
)