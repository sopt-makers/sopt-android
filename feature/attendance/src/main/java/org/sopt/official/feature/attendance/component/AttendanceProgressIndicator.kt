/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray400
import org.sopt.official.designsystem.Gray500
import org.sopt.official.designsystem.Gray700
import org.sopt.official.feature.attendance.model.ProgressBarUIState
import org.sopt.official.feature.attendance.R

@Composable
fun AttendanceProgressIndicator(
    progressState: ProgressBarUIState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // First Progress with label
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 12.dp)
        ) {
            ProgressCircle(
                isActive = progressState.isFirstProgressBarActive,
                isAttendance = progressState.isFirstProgressBarAttendance,
                type = ProgressCircleType.FIRST
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = stringResource(R.string.attendance_progress_first),
                color = if (progressState.isFirstProgressBarActive) Gray10 else Gray500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
        
        // First to Second Line - positioned at circle center height
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp) // Half of circle height to align with center
                .height(1.dp)
                .background(
                    if (progressState.isFirstToSecondLineActive) Gray10 else Gray400
                )
        )
        
        // Second Progress with label
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressCircle(
                isActive = progressState.isSecondProgressBarActive,
                isAttendance = progressState.isSecondProgressBarAttendance,
                type = ProgressCircleType.SECOND
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = stringResource(R.string.attendance_progress_second),
                color = if (progressState.isSecondProgressBarActive) Gray10 else Gray500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
        
        // Second to Third Line - positioned at circle center height
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp) // Half of circle height to align with center
                .height(1.dp)
                .background(
                    if (progressState.isSecondToThirdLineActive) Gray10 else Gray400
                )
        )
        
        // Third Progress with label
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 12.dp)
        ) {
            ThirdProgressCircle(
                isActive = progressState.isThirdProgressBarActive,
                isAttendance = progressState.isThirdProgressBarAttendance,
                isTardy = progressState.isThirdProgressBarTardy,
                isBeforeAttendance = progressState.isThirdProgressBarBeforeAttendance
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val thirdProgressText = when {
                progressState.isThirdProgressBarActiveAndBeforeAttendance -> {
                    if (progressState.isThirdProgressBarTardy) 
                        stringResource(R.string.attendance_progress_third_tardy)
                    else 
                        stringResource(R.string.attendance_progress_third_complete)
                }
                progressState.isThirdProgressBarActive -> {
                    stringResource(R.string.attendance_progress_third_absent)
                }
                else -> {
                    stringResource(R.string.attendance_progress_before)
                }
            }
            
            Text(
                text = thirdProgressText,
                color = if (progressState.isThirdProgressBarActive) Gray10 else Gray500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ProgressCircle(
    isActive: Boolean,
    isAttendance: Boolean,
    type: ProgressCircleType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isActive) {
            // Active circle with icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Gray10)
                    .border(1.dp, Gray300, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (isAttendance) 
                            R.drawable.ic_attendance_check_gray 
                        else 
                            R.drawable.ic_attendance_close_gray
                    ),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else {
            // Empty circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(1.dp, Gray400, CircleShape)
            )
        }
    }
}

@Composable
private fun ThirdProgressCircle(
    isActive: Boolean,
    isAttendance: Boolean,
    isTardy: Boolean,
    isBeforeAttendance: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isActive && isTardy -> {
                // Tardy state
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Gray700)
                        .border(1.dp, Gray10, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_point),
                        contentDescription = null,
                        tint = Gray10,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            isActive && isAttendance -> {
                // Attendance state
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Gray700)
                        .border(1.dp, Gray10, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            if (isBeforeAttendance)
                                R.drawable.ic_attendacne_check_white
                            else
                                R.drawable.ic_attendance_close_white
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            else -> {
                // Empty circle
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(1.dp, Gray400, CircleShape)
                )
            }
        }
    }
}

private enum class ProgressCircleType {
    FIRST, SECOND, THIRD
}