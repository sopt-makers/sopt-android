package org.sopt.official.feature.poke.v2.notification.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.theme.SoptTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokeNotificationTopAppBar(
    onNavigationIconClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "콕 찌르기",
                style = SoptTheme.typography.title4,
                color = SoptTheme.colors.fg.neutral.bold
            )
        },
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(MdsIcons.chevronLeftOutlined),
                contentDescription = null,
                tint = SoptTheme.colors.fg.neutral.bold,
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable(onClick = onNavigationIconClick)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SoptTheme.colors.bg.layer.basement
        )
    )
}
