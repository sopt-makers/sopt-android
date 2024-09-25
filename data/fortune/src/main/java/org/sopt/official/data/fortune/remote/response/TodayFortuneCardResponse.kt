package org.sopt.official.data.fortune.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodayFortuneCardResponse(
    @SerialName("description")
    val description: String,
    @SerialName("imageColorCode")
    val imageColorCode: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("name")
    val name: String,
)
