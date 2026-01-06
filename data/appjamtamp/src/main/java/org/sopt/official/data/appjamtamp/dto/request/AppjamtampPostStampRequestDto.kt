package org.sopt.official.data.appjamtamp.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppjamtampPostStampRequestDto(
    @SerialName("missionId")
    val missionId: Int,
    @SerialName("image")
    val image: String,
    @SerialName("contents")
    val contents: String,
    @SerialName("activityDate")
    val activityDate: String
)
