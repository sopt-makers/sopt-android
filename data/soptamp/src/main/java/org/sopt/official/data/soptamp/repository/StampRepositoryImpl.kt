/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.soptamp.repository

import org.sopt.official.data.soptamp.remote.api.StampService
import org.sopt.official.data.soptamp.remote.mapper.toData
import org.sopt.official.data.soptamp.remote.model.request.toData
import org.sopt.official.domain.soptamp.model.Archive
import org.sopt.official.domain.soptamp.model.Stamp
import org.sopt.official.domain.soptamp.model.StampClap
import org.sopt.official.domain.soptamp.model.StampClapResult
import org.sopt.official.domain.soptamp.model.StampClappers
import org.sopt.official.domain.soptamp.repository.StampRepository
import dev.zacsweers.metro.Inject

@Inject
class StampRepositoryImpl(
    private val service: StampService,
) : StampRepository {

    override suspend fun completeMission(stamp: Stamp): Result<Archive> {
        return runCatching {
            service.registerStamp(
                stamp.toData()
            ).toDomain()
        }
    }

    override suspend fun getMissionContent(missionId: Int, nickname: String): Result<Archive> {
        return runCatching {
            service.retrieveStamp(
                missionId = missionId,
                nickname = nickname
            ).toDomain()
        }
    }

    override suspend fun modifyMission(stamp: Stamp): Result<Unit> {
        return runCatching {
            service.modifyStamp(
                stamp.toData()
            )
        }
    }

    override suspend fun deleteMission(missionId: Int): Result<Unit> {
        return runCatching {
            service.deleteStamp(
                missionId = missionId
            )
        }
    }

    override suspend fun getReportUrl(): Result<String> = runCatching {
        service.getReportUrl().reportUrl
    }

    override suspend fun deleteAllStamps(): Result<Unit> {
        return runCatching { service.deleteAllStamps() }
    }

    override suspend fun getClappers(stampId: Int): Result<StampClappers> = runCatching {
        service.getClapUser(stampId).toDomain()
    }

    override suspend fun clapStamp(stampId: Int, clap: StampClap): Result<StampClapResult> = runCatching {
        service.postClapStamp(stampId, clap.toData()).toDomain()
    }
}
