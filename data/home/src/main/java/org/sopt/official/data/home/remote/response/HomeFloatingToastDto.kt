package org.sopt.official.data.home.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeFloatingToastDto(
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("title")
    val title: String,
    @SerialName("expandedSubTitle")
    val expandedSubTitle: String,
    @SerialName("collapsedSubtitle")
    val collapsedSubtitle: String,
    @SerialName("actionButtonName")
    val actionButtonName: String,
    @SerialName("linkUrl")
    val linkUrl: String,
    @SerialName("isActive")
    val isActive: Boolean
)
