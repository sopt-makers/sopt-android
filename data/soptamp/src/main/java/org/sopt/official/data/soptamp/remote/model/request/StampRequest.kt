package org.sopt.official.data.soptamp.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StampRequest(
    @SerialName("missionId")
    val missionId: Int,
    @SerialName("image")
    val image: String,
    @SerialName("contents")
    val contents: String,
    @SerialName("activityDate")
    val activityDate: String,
)
