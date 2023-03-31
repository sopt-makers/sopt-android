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
package org.sopt.official.stamp.data.remote.source

import org.sopt.stamp.data.error.ErrorData
import org.sopt.stamp.data.remote.api.SoptampService
import org.sopt.official.stamp.data.remote.mapper.toData
import org.sopt.official.stamp.data.remote.model.MissionData
import org.sopt.official.stamp.data.source.MissionsDataSource
import java.net.UnknownHostException
import javax.inject.Inject

internal class RemoteMissionsDataSource @Inject constructor(
    private val soptampService: SoptampService
) : MissionsDataSource {
    override suspend fun getAllMission(userId: Int): Result<List<MissionData>> {
        val result = kotlin.runCatching { soptampService.getAllMissions(userId).toData() }
        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is UnknownHostException -> return Result.failure(ErrorData.NetworkUnavailable)
            else -> Result.failure(exception)
        }
    }
}
