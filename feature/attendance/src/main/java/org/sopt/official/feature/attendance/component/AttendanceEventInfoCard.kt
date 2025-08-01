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
package org.sopt.official.feature.attendance.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.Orange500
import org.sopt.official.domain.attendance.entity.SoptEvent
import org.sopt.official.feature.attendance.model.ProgressBarUIState
import org.sopt.official.feature.attendance.R

@Composable
fun AttendanceEventInfoCard(
    soptEvent: SoptEvent,
    progressState: ProgressBarUIState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Gray800)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Event Date
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_attendance_event_date),
                    contentDescription = null,
                    tint = Gray300,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = soptEvent.date,
                    color = Gray300,
                    fontSize = 14.sp
                )
            }

            // Event Location
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_attendance_event_location),
                    contentDescription = null,
                    tint = Gray300,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = soptEvent.location,
                    color = Gray300,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Event Name
            val annotatedEventName = buildAnnotatedString {
                val message = soptEvent.message
                val eventName = soptEvent.eventName

                if (message.isNotEmpty() && eventName.contains(message)) {
                    val startIndex = eventName.indexOf(message)
                    val endIndex = startIndex + message.length

                    append(eventName.substring(0, startIndex))
                    withStyle(
                        style = SpanStyle(
                            color = Orange500,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(message)
                    }
                    append(eventName.substring(endIndex))
                } else {
                    append(eventName)
                }
            }

            Text(
                text = annotatedEventName,
                color = Gray10,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )

            if (!soptEvent.isAttendancePointAwardedEvent) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "이번 행사는 출석 점수가 부여되지 않습니다.",
                    color = Gray100,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Attendance Progress
            AttendanceProgressIndicator(
                progressState = progressState
            )
        }
    }
}
