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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray400
import org.sopt.official.designsystem.Gray600
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.Green400
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.Red400
import org.sopt.official.domain.attendance.entity.AttendanceHistory
import org.sopt.official.domain.attendance.entity.AttendanceLog
import org.sopt.official.domain.attendance.entity.AttendanceStatus
import org.sopt.official.domain.attendance.entity.AttendanceSummary

@Composable
fun AttendanceHistoryList(
    attendanceHistory: AttendanceHistory,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Gray800)
    ) {
        LazyColumn(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Info
            item {
                AttendanceUserInfoItem(
                    userInfo = attendanceHistory.userInfo
                )
            }

            // Summary
            item {
                AttendanceSummaryItem(
                    summary = attendanceHistory.attendanceSummary
                )
            }

            // Attendance Logs Header
            item {
                AttendanceLogHeaderItem()
            }

            // Attendance Logs
            items(attendanceHistory.attendanceLog) { log ->
                AttendanceLogItem(
                    log = log
                )
            }
        }
    }
}

@Composable
private fun AttendanceUserInfoItem(
    userInfo: org.sopt.official.domain.attendance.entity.AttendanceUserInfo
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${userInfo.userName} ${userInfo.partName}",
            color = Gray10,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${userInfo.generation}기",
            color = Gray300,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun AttendanceSummaryItem(
    summary: AttendanceSummary
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "출석",
                color = Gray300,
                fontSize = 14.sp
            )
            Text(
                text = "${summary.normal}회",
                color = Gray10,
                fontSize = 14.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "지각",
                color = Gray300,
                fontSize = 14.sp
            )
            Text(
                text = "${summary.late}회",
                color = Gray10,
                fontSize = 14.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "결석",
                color = Gray300,
                fontSize = 14.sp
            )
            Text(
                text = "${summary.abnormal}회",
                color = Gray10,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun AttendanceLogHeaderItem() {
    Divider(
        color = Gray600,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    Text(
        text = "출석 기록",
        color = Gray10,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun AttendanceLogItem(
    log: AttendanceLog
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = log.eventName,
                color = Gray10,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = log.date,
                color = Gray400,
                fontSize = 12.sp
            )
        }

        AttendanceStatusBadge(
            statusText = log.attendanceState
        )
    }
}

@Composable
private fun AttendanceStatusBadge(
    statusText: String
) {
    val (backgroundColor, textColor, text) = when (statusText) {
        "출석" -> Triple(
            Green400,
            Color.Black,
            "출석"
        )
        "지각" -> Triple(
            Orange400,
            Color.Black,
            "지각"
        )
        "결석" -> Triple(
            Gray600,
            Gray300,
            "결석"
        )
        "참석" -> Triple(
            Red400,
            Color.Black,
            "참석"
        )
        else -> Triple(
            Gray600,
            Gray300,
            statusText
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
