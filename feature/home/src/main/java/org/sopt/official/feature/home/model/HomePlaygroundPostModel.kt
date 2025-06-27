package org.sopt.official.feature.home.model

data class HomePlaygroundPostModel(
    val postId: Int,
    val title: String,
    val content: String,
    val category: String,
    val label: String,
    val profileImage: String,
    val name: String,
    val part: String?
)

data class HomePlaygroundEmptyPostModel(
    val category: String,
    val description: String,
    val iconUrl: String,
    val webLink: String
)
