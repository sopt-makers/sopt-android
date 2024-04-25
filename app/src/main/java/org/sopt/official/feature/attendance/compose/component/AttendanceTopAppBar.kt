package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.util.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceTopAppBar(actions: AttendanceTopAppBarActions) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.attendance),
                color = SoptTheme.colors.primary,
                style = SoptTheme.typography.body1
            )
        },
        modifier = Modifier,
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_attendance_arrow_left_white),
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .noRippleClickable(onClick = actions.onClickBackIcon)
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh),
                contentDescription = stringResource(R.string.refresh),
                modifier = Modifier
                    .padding(end = 20.dp)
                    .noRippleClickable(onClick = actions.onClickRefreshIcon)
            )
        },
        colors = TopAppBarColors(
            containerColor = SoptTheme.colors.background,
            scrolledContainerColor = SoptTheme.colors.background,
            navigationIconContentColor = SoptTheme.colors.onBackground,
            titleContentColor = SoptTheme.colors.onBackground,
            actionIconContentColor = SoptTheme.colors.onBackground,
        )
    )
}

class AttendanceTopAppBarActions(
    val onClickBackIcon: () -> Unit,
    val onClickRefreshIcon: () -> Unit,
)

@Preview
@Composable
private fun AttendanceTopAppBarPreview() {
    SoptTheme {
        AttendanceTopAppBar(
            AttendanceTopAppBarActions(
                onClickBackIcon = {},
                onClickRefreshIcon = {},
            )
        )
    }
}

