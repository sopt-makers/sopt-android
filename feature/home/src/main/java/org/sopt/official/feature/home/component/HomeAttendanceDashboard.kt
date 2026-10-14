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
package org.sopt.official.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.Schedule
import org.sopt.official.feature.home.model.Schedule.BREAK
import org.sopt.official.feature.home.model.Schedule.EVENT
import org.sopt.official.feature.home.model.Schedule.JOINT_SEMINAR
import org.sopt.official.feature.home.model.Schedule.SEMINAR
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.components.button.MdsActionButton
import org.sopt.official.mds.components.button.MdsActionButtonSize
import org.sopt.official.mds.components.button.MdsActionButtonType
import org.sopt.official.mds.components.tag.MdsTag
import org.sopt.official.mds.components.tag.MdsTagEmphasis
import org.sopt.official.mds.components.tag.MdsTagShape
import org.sopt.official.mds.components.tag.MdsTagSize
import org.sopt.official.mds.components.tag.MdsTagType
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun HomeSoptScheduleDashboard(
    homeSoptScheduleModel: HomeSoptScheduleModel,
    isActivatedGeneration: Boolean,
    onScheduleClick: () -> Unit,
    onAttendanceButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onScheduleClick),
        content = {
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 10.dp,
                )
            ) {
                Text(
                    text = homeSoptScheduleModel.formattedDate,
                    style = SoptTheme.typography.label3,
                    color = SoptTheme.colors.fg.neutral.subtle
                )

                Spacer(modifier = Modifier.width(width = 8.dp))

                HomeScheduleTypeChip(homeSoptScheduleModel.type)

                Spacer(modifier = Modifier.width(width = 8.dp))

                Text(
                    text = homeSoptScheduleModel.title,
                    style = SoptTheme.typography.title5,
                    color = SoptTheme.colors.fg.neutral.bold,
                )

                Icon(
                    imageVector = ImageVector.vectorResource(MdsIcons.chevronRightOutlined),
                    contentDescription = null,
                    tint = SoptTheme.colors.fg.neutral.bold,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.weight(weight = 1f))

                if (isActivatedGeneration) {
                    MdsActionButton(
                        text = "출석",
                        type = MdsActionButtonType.PRIMARY,
                        size = MdsActionButtonSize.SMALL,
                        prefixIcon = MdsIcons.checkCircleFilled,
                        onClick = onAttendanceButtonClick,
                    )
                }
            }
        }
    )
}

@Composable
private fun HomeScheduleTypeChip(
    schedule: Schedule,
    modifier: Modifier = Modifier,
) {
    val tagType = when (schedule) {
        EVENT -> MdsTagType.SECONDARY
        SEMINAR -> MdsTagType.PRIMARY
        JOINT_SEMINAR -> MdsTagType.PRIMARY
        BREAK -> MdsTagType.DEFAULT
    }

    MdsTag(
        text = schedule.titleKR,
        type = tagType,
        emphasis = MdsTagEmphasis.SUBTLE,
        size = MdsTagSize.SMALL,
        shape = MdsTagShape.RECT,
        modifier = modifier
    )
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
