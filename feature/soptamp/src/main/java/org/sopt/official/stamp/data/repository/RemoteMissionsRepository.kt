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
package org.sopt.official.stamp.data.repository

import org.sopt.official.stamp.data.error.ErrorData
import org.sopt.official.stamp.data.mapper.toDomain
import org.sopt.official.stamp.data.source.MissionsDataSource
import org.sopt.official.stamp.domain.model.Mission
import org.sopt.official.stamp.domain.repository.MissionsRepository
import javax.inject.Inject

internal class RemoteMissionsRepository @Inject constructor(
    private val remote: MissionsDataSource
) : MissionsRepository {
    override suspend fun getAllMissions(userId: Int): Result<List<Mission>> {
        val result = remote.getAllMission(userId)
            .mapCatching { it.toDomain() }
        val exception = result.exceptionOrNull()
        return if (exception is ErrorData) {
            Result.failure(exception.toDomain())
        } else {
            result
        }
    }

    override suspend fun getCompleteMissions(userId: Int): Result<List<Mission>> {
        val result = remote.getAllMission(userId)
            .mapCatching {
                it.filter { mission -> mission.isCompleted }.toDomain()
            }
        val exception = result.exceptionOrNull()
        return if (exception is ErrorData) {
            Result.failure(exception.toDomain())
        } else {
            result
        }
    }

    override suspend fun getInCompleteMissions(userId: Int): Result<List<Mission>> {
        val result = remote.getAllMission(userId)
            .mapCatching {
                it.filter { mission -> !mission.isCompleted }.toDomain()
            }
        val exception = result.exceptionOrNull()
        return if (exception is ErrorData) {
            Result.failure(exception.toDomain())
        } else {
            result
        }
    }
}
