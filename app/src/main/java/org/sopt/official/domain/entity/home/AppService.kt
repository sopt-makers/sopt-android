package org.sopt.official.domain.entity.home

data class AppService(
    val serviceName: String,
    val activeUser: Boolean,
    val inactiveUser: Boolean
)
