package org.sopt.official.data.home.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeLatestPostsResponseDto(
    @SerialName("recentPosts")
    val recentPosts: List<HomeLatestPostResponseDto>
)

@Serializable
data class HomeLatestPostResponseDto(
    @SerialName("userId")
    val userId: Int?,
    @SerialName("profileImage")
    val profileImage: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("generationAndPart")
    val generationAndPart: String?,
    @SerialName("category")
    val category: String,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("webLink")
    val webLink: String,
    @SerialName("id")
    val id: Int?,
    @SerialName("isOutdated")
    val isOutdated: Boolean
)
