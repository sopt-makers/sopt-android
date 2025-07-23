package org.sopt.official.common.config.remoteconfig

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateConfigModel(
    @SerialName("latestVersion")
    val latestVersion: String,
    @SerialName("forcedUpdateNotice")
    val forcedUpdateNotice: String,
    @SerialName("optionalUpdateNotice")
    val optionalUpdateNotice: String
)
