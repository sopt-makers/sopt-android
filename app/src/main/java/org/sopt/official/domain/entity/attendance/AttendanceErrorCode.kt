/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
