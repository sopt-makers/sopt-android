package org.sopt.official.feature.mypage.component

import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageTopBar(
    @StringRes title: Int,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = title),
                style = SoptTheme.typography.body16M
            )
        },
        navigationIcon = {
            IconButton(onClick = onIconClick) {
                Icon(
                    painterResource(R.drawable.btn_arrow_left),
                    contentDescription = null,
                    tint = SoptTheme.colors.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SoptTheme.colors.background,
            titleContentColor = SoptTheme.colors.onBackground,
            actionIconContentColor = SoptTheme.colors.primary
        )
    )
}