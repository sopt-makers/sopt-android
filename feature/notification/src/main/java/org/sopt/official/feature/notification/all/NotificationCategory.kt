package org.sopt.official.feature.notification.all

enum class NotificationCategory(
    val category: String,
    val serverCode: String,
) {
    ALL("전체알림", "ALL"),
    NOTICE("공지", "NOTICE"),
    NEWS("소식", "NEWS")
}