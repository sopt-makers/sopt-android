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
package org.sopt.official.data.soptamp.remote.source

import org.sopt.official.data.soptamp.error.ErrorData
import org.sopt.official.data.soptamp.remote.api.RankService
import org.sopt.official.data.soptamp.remote.mapper.toData
import org.sopt.official.data.soptamp.remote.model.RankData
import org.sopt.official.data.soptamp.source.RankingDataSource
import java.net.UnknownHostException
import javax.inject.Inject

internal class RemoteRankingDataSource @Inject constructor(
    private val rankService: RankService
) : RankingDataSource {
    override suspend fun getRanking(): Result<List<RankData>> {
        return runCatching {
            rankService.getRanking().toData()
        }.validate()
    }

    override suspend fun getCurrentTermRanking(): Result<List<RankData>> {
        return runCatching {
            rankService.getCurrentRanking().toData()
        }.validate()
    }

    private fun Result<List<RankData>>.validate() = when (val exception = exceptionOrNull()) {
        null -> this
        is UnknownHostException -> Result.failure(ErrorData.NetworkUnavailable)
        else -> Result.failure(exception)
    }
}
