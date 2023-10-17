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
package org.sopt.official.domain.entity.attendance

import org.sopt.official.data.model.attendance.AttendanceCodeResponse

enum class AttendanceErrorCode(
    val attendanceErrorCode: AttendanceCodeResponse,
    val messages: List<String>
) {
    WRONG_CODE(AttendanceCodeResponse(-2), listOf("[LectureException] : 코드가 일치하지 않아요!")),
    BEFORE_ATTENDANCE(AttendanceCodeResponse(-1), listOf("[LectureException] : 1차 출석 시작 전입니다", "[LectureException] : 2차 출석 시작 전입니다")),
    AFTER_ATTENDANCE(
        AttendanceCodeResponse(0),
        listOf("[LectureException] : 1차 출석이 이미 종료되었습니다.", "[LectureException] : 2차 출석이 이미 종료되었습니다.")
    );

    companion object {
        fun of(message: String) = entries.find { it.messages.contains(message) }?.attendanceErrorCode
    }
}
