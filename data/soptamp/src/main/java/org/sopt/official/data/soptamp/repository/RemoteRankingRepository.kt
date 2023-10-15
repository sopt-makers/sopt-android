/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.data.soptamp.repository

import org.sopt.official.data.soptamp.error.ErrorData
import org.sopt.official.data.soptamp.mapper.toDomain
import org.sopt.official.data.soptamp.remote.api.RankService
import org.sopt.official.data.soptamp.source.RankingDataSource
import org.sopt.official.domain.soptamp.model.Rank
import org.sopt.official.domain.soptamp.model.RankFetchType
import org.sopt.official.domain.soptamp.repository.RankingRepository
import javax.inject.Inject

internal class RemoteRankingRepository @Inject constructor(
    private val remote: RankingDataSource,
    private val service: RankService
) : RankingRepository {
    override suspend fun getRanking(type: RankFetchType): Result<List<Rank>> {
        val result = when (type) {
            is RankFetchType.All -> remote.getRanking()
            is RankFetchType.Term -> remote.getCurrentTermRanking()
        }.mapCatching { it.toDomain() }
        val exception = result.exceptionOrNull()
        return if (exception is ErrorData) {
            Result.failure(exception.toDomain())
        } else {
            result
        }
    }

    override suspend fun getRankDetail(nickname: String) = runCatching {
        service.getRankDetail(nickname).toEntity()
    }
}
