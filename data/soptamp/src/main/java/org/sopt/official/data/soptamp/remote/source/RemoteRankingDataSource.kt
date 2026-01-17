/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.data.soptamp.remote.source

import java.net.UnknownHostException
import dev.zacsweers.metro.Inject
import org.sopt.official.data.soptamp.error.ErrorData
import org.sopt.official.data.soptamp.remote.api.RankService
import org.sopt.official.data.soptamp.remote.mapper.toData
import org.sopt.official.data.soptamp.remote.model.RankData
import org.sopt.official.data.soptamp.source.RankingDataSource

@Inject
class RemoteRankingDataSource(
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
