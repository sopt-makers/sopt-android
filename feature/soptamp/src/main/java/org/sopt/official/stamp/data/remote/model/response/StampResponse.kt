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
package org.sopt.official.stamp.data.remote.model.response

import kotlinx.serialization.Serializable
import org.sopt.official.stamp.domain.model.Archive

@Serializable
data class StampResponse(
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val id: Int,
    val contents: String,
    val images: List<String>? = null,
    val userId: Int,
    val missionId: Int
) {
    fun toDomain() = Archive(
        createdAt = createdAt,
        updatedAt = updatedAt,
        id = id,
        contents = contents,
        images = images ?: emptyList(),
        userId = userId,
        missionId = missionId
    )
}
