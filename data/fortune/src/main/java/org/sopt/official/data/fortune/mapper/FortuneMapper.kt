package org.sopt.official.data.fortune.mapper

import org.sopt.official.data.fortune.remote.response.TodayFortuneCardResponse
import org.sopt.official.data.fortune.remote.response.TodayFortuneWordResponse
import org.sopt.official.domain.fortune.model.TodayFortuneCard
import org.sopt.official.domain.fortune.model.TodayFortuneWord

internal fun TodayFortuneCardResponse.toDomain(): TodayFortuneCard = TodayFortuneCard(
    description = description,
    imageColorCode = imageColorCode,
    imageUrl = imageUrl,
    name = name,
)

internal fun TodayFortuneWordResponse.toDomain(): TodayFortuneWord = TodayFortuneWord(
    userName = userName,
    title = title,
)
