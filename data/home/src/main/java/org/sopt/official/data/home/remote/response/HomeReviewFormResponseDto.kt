package org.sopt.official.data.home.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeReviewFormResponseDto(
    @SerialName("title")
    val title: String,
    @SerialName("subTitle")
    val subTitle: String,
    @SerialName("actionButtonName")
    val actionButtonName: String,
    @SerialName("linkUrl")
    val linkUrl: String,
    @SerialName("isActive")
    val isActive: Boolean
)
