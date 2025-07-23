package org.sopt.official.domain.home.model

data class LatestPost(
    val profileImage: String,
    val name: String,
    val generationAndPart: String,
    val category: String,
    val title: String,
    val content: String,
    val webLink: String,
    val id: Int,
    val isOutdated: Boolean
)
