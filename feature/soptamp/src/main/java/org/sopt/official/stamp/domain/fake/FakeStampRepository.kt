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
package org.sopt.official.stamp.domain.fake

import org.sopt.official.stamp.domain.model.Archive
import org.sopt.official.stamp.domain.repository.StampRepository
import org.sopt.stamp.feature.mission.model.ImageModel

object FakeStampRepository : StampRepository {
    private val fakeArchive = Archive(
        id = 1,
        contents = "",
        images = listOf(""),
        userId = 1,
        missionId = 1
    )

    override suspend fun completeMission(
        missionId: Int,
        imageUri: ImageModel,
        content: String
    ): Result<Unit> = runCatching { }

    override suspend fun getMissionContent(missionId: Int) = runCatching { fakeArchive }
    override suspend fun getMissionContent(userId: Int, missionId: Int) = runCatching { fakeArchive }

    override suspend fun modifyMission(
        missionId: Int,
        imageUri: ImageModel,
        content: String
    ) = runCatching { }

    override suspend fun deleteMission(missionId: Int) = runCatching { }

    override suspend fun deleteAllStamps(userId: Int) = runCatching { }
}
