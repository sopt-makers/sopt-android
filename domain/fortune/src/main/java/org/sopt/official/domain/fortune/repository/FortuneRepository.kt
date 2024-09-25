package org.sopt.official.domain.fortune.repository

import org.sopt.official.domain.fortune.model.TodayFortuneCard
import org.sopt.official.domain.fortune.model.TodayFortuneWord

interface FortuneRepository {
    suspend fun fetchTodayFortuneWord(date: String): TodayFortuneWord
    suspend fun fetchTodayFortuneCard(): TodayFortuneCard
}
