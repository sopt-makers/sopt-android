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
package org.sopt.official.stamp.domain.model

enum class MissionsFilter(
    val title: String
) {
    ALL_MISSION("전체 미션"),
    COMPLETE_MISSION("완료 미션"),
    INCOMPLETE_MISSION("미완료 미션");

    fun hasTitle(title: String) = (title == this.title)

    companion object {
        fun getTitleOfMissionsList(): List<String> = entries.map { it.title }
        fun findFilterOf(title: String) = entries.find { it.hasTitle(title) }
            ?: throw IllegalArgumentException("$title 에 해당하는 필터가 없습니다.")
    }
}
