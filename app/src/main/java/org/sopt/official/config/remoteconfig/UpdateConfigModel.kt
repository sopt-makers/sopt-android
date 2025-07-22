package org.sopt.official.config.remoteconfig

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateConfigModel(
    @SerialName("minimumVersion")
    val minimumVersion: String,
    @SerialName("forcedUpdateNotice")
    val forcedUpdateNotice: String,
    @SerialName("optionalUpdateNotice")
    val optionalUpdateNotice: String
)
