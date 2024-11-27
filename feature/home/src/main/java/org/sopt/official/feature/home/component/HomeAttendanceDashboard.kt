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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.home.R.drawable.ic_arrow_right
import org.sopt.official.feature.home.R.drawable.ic_check_filled

@Composable
internal fun HomeSoptScheduleDashboard(
    date: String,
    scheduleType: String,
    scheduleTitle: String,
    onDashboardClick: () -> Unit,
    onAttendanceButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDashboardClick() },
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
                    text = date,
                    style = typography.title14SB,
                    color = colors.onSurface400,
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                HomeScheduleTypeChip(scheduleType)
                Spacer(modifier = Modifier.width(width = 8.dp))
                Text(
                    text = scheduleTitle,
                    style = typography.title16SB,
                    color = colors.onBackground,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(ic_arrow_right),
                    contentDescription = null,
                    tint = Unspecified,
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                HomeAttendanceButton(onButtonClick = onAttendanceButtonClick)
            }
        }
    )
}

@Composable
private fun HomeScheduleTypeChip(
    scheduleType: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(
            color = Color.Blue,
            shape = RoundedCornerShape(size = 4.dp)
        )
    ) {
        Text(
            text = scheduleType,
            style = typography.label11SB,
            color = colors.onSurface400,
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 3.dp,
            )
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
            date = "TODO()",
            scheduleType = "TODO()",
            scheduleTitle = "TODO()",
            onDashboardClick = {},
            onAttendanceButtonClick = {},
        )
    }
}
