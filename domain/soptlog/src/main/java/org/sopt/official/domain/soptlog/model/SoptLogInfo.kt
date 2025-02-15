package org.sopt.official.domain.soptlog.model

data class SoptLogInfo(
    val userName: String,
    val profileImageUrl: String,
    val part: String,
    val soptampRank: String,
    val pokeCount: String,
    val soptLevel: String,
    val during: String,
    val profileMessage: String?,
    val isActive: Boolean,
    val todayFortuneTitle: String,
)
