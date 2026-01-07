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
package org.sopt.official.feature.appjamtamp.missionlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.model.MissionFilter

@Composable
internal fun DropDownHeader(
    title: String,
    modifier: Modifier = Modifier,
    onMenuClick: (String) -> Unit = {},
    onReportButtonClick: () -> Unit = {},
    onEditMessageButtonClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = SoptTheme.typography.heading18B,
            color = SoptTheme.colors.onSurface10
        )

        DropDownMenuButton(
            menuTexts = MissionFilter.getTitleOfMissionsList(),
            onMenuClick = onMenuClick,
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.WarningAmber,
                contentDescription = null,
                tint = SoptTheme.colors.onSurface10,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onReportButtonClick)
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_missionlist_edit_message),
                contentDescription = null,
                tint = SoptTheme.colors.onSurface10,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onEditMessageButtonClick)
            )
        }
    }
}

@Composable
private fun DropDownMenuButton(
    menuTexts: ImmutableList<String>,
    onMenuClick: (String) -> Unit = {},
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(3) }
    Box {
        Icon(
            imageVector = ImageVector.vectorResource(
                if (isMenuExpanded) {
                    R.drawable.ic_arrow_up_32
                } else {
                    R.drawable.ic_arrow_down_32
                }
            ),
            contentDescription = null,
            tint = SoptTheme.colors.onSurface10,
            modifier = Modifier
                .clickable {
                    isMenuExpanded = !isMenuExpanded
                }
        )
        DropdownMenu(
            modifier =
                Modifier
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                    )
                    .padding(vertical = 12.dp),
            expanded = isMenuExpanded,
            offset = DpOffset((-69).dp, 12.dp),
            onDismissRequest = { isMenuExpanded = false },
        ) {
            menuTexts.forEach { menuText ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = menuText,
                            style = SoptTheme.typography.heading16B,
                            color = if (menuText == menuTexts[selectedIndex]) SoptTheme.colors.onSurface else SoptTheme.colors.onSurface400,
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 20.dp)
                        )
                    },
                    onClick = {
                        selectedIndex = menuTexts.indexOf(menuText)
                        onMenuClick(menuText)
                        isMenuExpanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DropDownHeaderPreview() {
    SoptTheme {
        DropDownHeader(
            title = "앱잼 미션"
        )
    }
}
