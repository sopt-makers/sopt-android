package org.sopt.official.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.R.drawable.ic_notification_off
import org.sopt.official.feature.home.R.drawable.ic_notification_on
import org.sopt.official.feature.home.R.drawable.ic_setting
import org.sopt.official.feature.home.R.drawable.img_logo

@Composable
internal fun HomeTopBar(
    hasNotification: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(img_logo),
            contentDescription = null,
            modifier = Modifier.size(
                width = 72.dp,
                height = 40.dp,
            )
        )
        Row(
            horizontalArrangement = spacedBy(space = 10.dp),
            verticalAlignment = CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    if (hasNotification) ic_notification_on else ic_notification_off
                ),
                contentDescription = null,
                tint = Unspecified,
            )
            Icon(
                imageVector = ImageVector.vectorResource(ic_setting),
                contentDescription = null,
                tint = Unspecified,
            )
        }
    }
}

@Preview
@Composable
private fun HomeTopBarPreview() {
    SoptTheme {
        HomeTopBar(
            hasNotification = true,
        )
    }
}
