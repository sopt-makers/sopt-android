package org.sopt.official.data.sopletter.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SopletterTopicsResponseDto(
    @SerialName("topics")
    val topics: List<SopletterTopicDto>,
)

@Serializable
data class SopletterTopicDto(
    @SerialName("topicId")
    val topicId: Long,
    @SerialName("title")
    val title: String,
)
