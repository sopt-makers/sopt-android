package org.sopt.official.feature.notice.model

data class NoticeDetailModel(
    val id: Int,
    val title: String,
    val creator: String,
    val createdAt: String,
    val images: List<String>,
    val content: String,
    val part: String,
    val scope: String
)
