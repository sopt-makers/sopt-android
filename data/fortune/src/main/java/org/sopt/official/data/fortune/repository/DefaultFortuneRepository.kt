package org.sopt.official.data.fortune.repository

import org.sopt.official.data.fortune.remote.api.FortuneApi
import javax.inject.Inject

class DefaultFortuneRepository @Inject constructor(
    private val fortuneApi: FortuneApi,
) {

    suspend fun fetchTodayFortuneWord() {
        fortuneApi
    }

    suspend fun fetchTodayFortuneCard() {
        fortuneApi
    }
}
