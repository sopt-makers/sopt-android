package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.clickable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceTopAppBar(
    onClickBackIcon: () -> Unit,
    onClickRefreshIcon: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.attendance_app_bar_title),
                color = SoptTheme.colors.onSurface10,
                style = SoptTheme.typography.body16M
            )
        },
        modifier = modifier,
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.btn_arrow_left),
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable(onClick = onClickBackIcon)
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh),
                contentDescription = stringResource(R.string.refresh),
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable(onClick = onClickRefreshIcon)
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

@Preview
@Composable
private fun AttendanceTopAppBarPreview() {
    SoptTheme {
        AttendanceTopAppBar(
            onClickBackIcon = {},
            onClickRefreshIcon = {},
        )
    }
}
