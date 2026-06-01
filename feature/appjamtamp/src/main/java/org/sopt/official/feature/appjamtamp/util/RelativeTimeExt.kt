/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

private val seoulZoneId: ZoneId = ZoneId.of("Asia/Seoul")
private val monthDayFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("M월d일", Locale.KOREA)

fun String?.toRelativeTime(): String {
    if (this.isNullOrBlank()) return ""

    val dateTime = runCatching {
        LocalDateTime.parse(this).atZone(seoulZoneId)
    }.getOrNull() ?: return ""

    val currentDateTime = ZonedDateTime.now(seoulZoneId)
    val diffMillis = currentDateTime.toInstant().toEpochMilli() - dateTime.toInstant().toEpochMilli()

    if (diffMillis < 0) return "방금 전"

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

    return when {
        minutes < 10L -> "방금 전"
        minutes < 60L -> "${minutes}분 전"
        hours < 25L -> "${hours}시간 전"
        days < 7L -> "${days}일 전"
        days < 35L -> "${days / 7}주 전"
        else -> dateTime.format(monthDayFormatter)
    }
}
