package org.sopt.official.feature.home.model

data class HomePlaygroundFeedModel(
    val postId: Int,
    val title: String,
    val content: String,
    val category: String,
    val label: String,
    val profileImage: String,
    val name: String,
    val part: String
)
