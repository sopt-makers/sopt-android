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
package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Blue400
import org.sopt.official.designsystem.BlueAlpha200
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.OrangeAlpha200
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.home.R.drawable.ic_arrow_right
import org.sopt.official.feature.home.R.drawable.ic_check_filled
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.Schedule
import org.sopt.official.feature.home.model.Schedule.EVENT
import org.sopt.official.feature.home.model.Schedule.SEMINAR

@Composable
fun HomeSoptScheduleDashboard(
    homeSoptScheduleModel: HomeSoptScheduleModel,
    isActivatedGeneration: Boolean,
    onScheduleClick: () -> Unit,
    onAttendanceButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onScheduleClick() },
        content = {
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 10.dp,
                ),
            ) {
                Text(
                    text = homeSoptScheduleModel.formattedDate,
                    style = typography.title14SB,
                    color = colors.onSurface400,
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                HomeScheduleTypeChip(homeSoptScheduleModel.type)
                Spacer(modifier = Modifier.width(width = 8.dp))
                Text(
                    text = homeSoptScheduleModel.title,
                    style = typography.title16SB,
                    color = colors.onBackground,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(ic_arrow_right),
                    contentDescription = null,
                    tint = Unspecified,
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                if (isActivatedGeneration) HomeAttendanceButton(onButtonClick = onAttendanceButtonClick)
            }
        }
    )
}

@Composable
private fun HomeScheduleTypeChip(
    schedule: Schedule,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(
            color = when (schedule) {
                EVENT -> BlueAlpha200
                SEMINAR -> OrangeAlpha200
            },
            shape = RoundedCornerShape(size = 4.dp),
        )
    ) {
        Text(
            text = schedule.titleKR,
            style = typography.label11SB,
            color = when (schedule) {
                EVENT -> Blue400
                SEMINAR -> Orange400
            },
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 3.dp,
            ),
        )
    }
}

@Composable
private fun HomeAttendanceButton(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = colors.onBackground),
        shape = RoundedCornerShape(size = 8.dp),
        contentPadding = PaddingValues(
            vertical = 10.dp,
            horizontal = 14.dp,
        ),
        onClick = onButtonClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_check_filled),
            contentDescription = null,
            tint = Unspecified,
        )
        Spacer(modifier = Modifier.width(width = 4.dp))
        Text(
            text = "출석",
            style = typography.label14SB,
            color = colors.background,
        )
    }
}

@Preview
@Composable
private fun HomeAttendanceDashboardPreview() {
    SoptTheme {
        HomeSoptScheduleDashboard(
            HomeSoptScheduleModel(
                date = "TODO()",
                title = "TODO()",
            ),
            isActivatedGeneration = false,
            onScheduleClick = {},
            onAttendanceButtonClick = {},
        )
    }
}
