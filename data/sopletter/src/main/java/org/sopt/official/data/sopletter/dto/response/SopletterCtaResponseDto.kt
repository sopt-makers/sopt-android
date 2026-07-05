package org.sopt.official.data.sopletter.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SopletterCtaResponseDto(
    @SerialName("showCta")
    val showCta: Boolean,
    @SerialName("topicId")
    val topicId: Long?,
    @SerialName("ctaText")
    val ctaText: String?,
)
