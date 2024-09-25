package org.sopt.official.data.fortune.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TodayFortuneWordResponse(
    @SerialName("userName")
    val userName: String,
    @SerialName("title")
    val title: String,
)
