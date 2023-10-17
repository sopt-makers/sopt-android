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
package org.sopt.official.domain.repository.attendance

import org.sopt.official.data.model.attendance.AttendanceCodeResponse
import org.sopt.official.domain.entity.attendance.AttendanceHistory
import org.sopt.official.domain.entity.attendance.AttendanceRound
import org.sopt.official.domain.entity.attendance.SoptEvent

interface AttendanceRepository {
    suspend fun fetchSoptEvent(): Result<SoptEvent>
    suspend fun fetchAttendanceHistory(): Result<AttendanceHistory>
    suspend fun fetchAttendanceRound(lectureId: Long): Result<AttendanceRound>
    suspend fun confirmAttendanceCode(
        subLectureId: Long,
        code: String
    ): Result<AttendanceCodeResponse>
}
