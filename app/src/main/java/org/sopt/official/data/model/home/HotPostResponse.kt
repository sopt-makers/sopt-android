package org.sopt.official.data.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.home.HotPostEntity

@Serializable
data class HotPostResponse(
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("url")
    val url: String,
) {
    fun toEntity(): HotPostEntity = HotPostEntity(
        title = this.title,
        content = this.content,
        url = this.url
    )
}
