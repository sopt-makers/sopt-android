package org.sopt.official.config.remoteconfig

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateConfigResponseDto(
    @SerialName("latestVersion")
    val latestVersion: String,
    @SerialName("forcedUpdateNotice")
    val forcedUpdateNotice: String,
    @SerialName("optionalUpdateNotice")
    val optionalUpdateNotice: String
)
