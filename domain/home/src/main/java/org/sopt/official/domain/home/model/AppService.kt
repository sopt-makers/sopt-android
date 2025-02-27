package org.sopt.official.domain.home.model

data class AppService(
    val serviceName: String,
    val displayAlarmBadge: Boolean,
    val alarmBadge: String,
    val iconUrl: String,
    val deepLink: String,
)
