package org.sopt.official.data.fortune.repository

import org.sopt.official.data.fortune.mapper.toDomain
import org.sopt.official.data.fortune.remote.api.FortuneApi
import org.sopt.official.domain.fortune.model.TodayFortuneCard
import org.sopt.official.domain.fortune.model.TodayFortuneWord
import org.sopt.official.domain.fortune.repository.FortuneRepository
import javax.inject.Inject

internal class DefaultFortuneRepository @Inject constructor(
    private val fortuneApi: FortuneApi,
) : FortuneRepository {

    override suspend fun fetchTodayFortuneWord(date: String): TodayFortuneWord = fortuneApi.getTodayFortuneWord(date).toDomain()

    override suspend fun fetchTodayFortuneCard(): TodayFortuneCard = fortuneApi.getTodayFortuneCard().toDomain()
}
