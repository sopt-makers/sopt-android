/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.notification.all.component

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.util.Date
import java.util.Locale
import org.sopt.official.domain.notification.entity.NotificationItem
import org.sopt.official.mds.theme.SoptTheme

private const val ONE_DAY_IN_MILLISECONDS = 86400000L
private const val ONE_HOUR_IN_MILLISECONDS = 3600000L
private const val ONE_MINUTE_IN_MILLISECONDS = 60000L

@Composable
fun NotificationInfoItem(
    notification: NotificationItem?,
    onCLick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCLick)
            .background(
                if (notification?.isRead == true) {
                    Color.Transparent
                } else {
                    SoptTheme.colors.bg.neutral.ghost
                }
            )
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = notification?.title.orEmpty(),
                style = SoptTheme.typography.title5,
                color = SoptTheme.colors.fg.neutral.default,
                modifier = Modifier
                    .weight(1f)
                    .widthIn(max = 250.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = notification?.createdAt.orEmpty().convertToTimesAgo(),
                style = SoptTheme.typography.label4,
                color = SoptTheme.colors.fg.neutral.ghost
            )
        }

        Text(
            text = notification?.content.orEmpty(),
            style = SoptTheme.typography.body2,
            color = SoptTheme.colors.fg.neutral.subtle,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
    HorizontalDivider(color = SoptTheme.colors.stroke.neutral.ghost)
}

private fun String.convertToTimesAgo(): String {
    if (this.isEmpty()) return ""

    val dateFormat: DateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
        Locale.KOREA
    )
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

    val currentDate = Date()
    val receivedDate = dateFormat.parse(this)
    val diffInMillis = currentDate.time - receivedDate.time

    val diffInDays = diffInMillis / ONE_DAY_IN_MILLISECONDS
    val diffInHours = diffInMillis / ONE_HOUR_IN_MILLISECONDS
    val diffInMinutes = diffInMillis / ONE_MINUTE_IN_MILLISECONDS

    return when {
        diffInDays >= 1 -> "${diffInDays}일 전"
        diffInHours >= 1 -> "${diffInHours}시간 전"
        diffInMinutes >= 1 -> "${diffInMinutes}분 전"
        else -> "방금"
    }
}
