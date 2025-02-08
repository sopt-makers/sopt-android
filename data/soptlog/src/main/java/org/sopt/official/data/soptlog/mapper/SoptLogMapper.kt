package org.sopt.official.data.soptlog.mapper

import org.sopt.official.data.soptlog.dto.SoptLogInfoResponse
import org.sopt.official.domain.soptlog.model.SoptLogInfo

internal fun SoptLogInfoResponse.toDomain() = SoptLogInfo(
    profileImageUrl = profileImageUrl,
    userName = userName,
    part = part,
    soptampRank = soptampRank,
    pokeCount = pokeCount,
    soptLevel = soptLevel,
    during = during?:"-",
    profileMessage = profileMessage,
    isActive = isActive,
    todayFortuneTitle = todayFortuneTitle
)
