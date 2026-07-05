package org.sopt.official.data.sopletter.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SopletterWriteRequest(
    @SerialName("content")
    val content: String
)