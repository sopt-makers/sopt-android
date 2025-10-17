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
package org.sopt.official.feature.attendance.model

object AttendanceConstants {
    // Response codes
    const val ERROR_CODE = -2L
    const val NO_SESSION_CODE = -1L
    const val TIME_RESTRICTION_CODE = 0L

    // Error messages
    const val ERROR_WRONG_CODE = "코드가 일치하지 않아요!"
    const val ERROR_BEFORE_TIME = "출석 시간 전입니다."
    const val ERROR_AFTER_TIME = "출석이 이미 종료되었습니다."
    const val ERROR_INVALID_CODE = "출석 코드가 올바르지 않습니다"

    // Attendance texts
    const val FIRST_ATTENDANCE_TEXT = "1차 출석"
    const val SECOND_ATTENDANCE_TEXT = "2차 출석"

    // UI dimensions
    const val DIALOG_WIDTH_DP = 258
    const val CODE_INPUT_FIELDS = 5
    const val GRADIENT_HEIGHT_DP = 156
    const val BOTTOM_PADDING_DP = 100
}
