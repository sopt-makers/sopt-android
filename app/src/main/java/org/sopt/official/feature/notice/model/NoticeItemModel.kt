package org.sopt.official.feature.notice.model

data class NoticeItemModel(
    val title: String,
    val isNewNotice: Boolean,
    val creator: String,
    val createdAt: String
)
