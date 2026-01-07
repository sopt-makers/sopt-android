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
