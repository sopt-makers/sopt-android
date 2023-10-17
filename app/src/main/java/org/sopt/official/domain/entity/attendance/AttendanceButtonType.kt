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

enum class AttendanceButtonType(
    val attendanceRound: AttendanceRound,
    val messages: List<String>,
) {
    GONE_BUTTON(AttendanceRound(-1, "-"), listOf("[LectureException] : 오늘 세션이 없습니다.", "[LectureException] : 존재하지 않는 세션입니다.")),
    BEFORE_FIRST_ATTENDANCE(
        AttendanceRound(0, "1차 출석 전"),
        listOf("[LectureException] : 출석 시작 전입니다.", "[LectureException] : 1차 출석 시작 전입니다.")
    ),
    BEFORE_SECOND_ATTENDANCE(
        AttendanceRound(0, "2차 출석 전"),
        listOf("[LectureException] : 2차 출석 시작 전입니다.", "[LectureException] : 1차 출석이 이미 종료되었습니다.")
    ),
    AFTER_SECOND_ATTENDANCE(AttendanceRound(0, "2차 출석 종료"), listOf("[LectureException] : 2차 출석이 이미 종료되었습니다.")),
    ERROR(AttendanceRound(-2, ""), listOf());

    companion object {
        fun of(message: String) = entries.find { it.messages.contains(message) }?.attendanceRound
            ?: ERROR.attendanceRound
    }
}
