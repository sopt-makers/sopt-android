package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R

@Composable
internal fun DetailInfo(
    date: String,
    clapCount: Int,
    viewCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date,
            style = SoptTheme.typography.body13M,
            color = SoptTheme.colors.onSurface300
        )

        Spacer(modifier = Modifier.weight(1f))

        IconText(
            icon = ImageVector.vectorResource(R.drawable.ic_clap_gray),
            value = clapCount
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconText(
            icon = ImageVector.vectorResource(R.drawable.ic_view_count),
            value = viewCount
        )
    }
}

@Composable
private fun IconText(
    icon: ImageVector,
    value: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SoptTheme.colors.onSurface300
        )

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = value.toString(),
            style = SoptTheme.typography.body13M,
            color = SoptTheme.colors.onSurface300
        )
    }
}

@Preview
@Composable
private fun DetailInfoPreview() {
    SoptTheme {
        DetailInfo(
            date = "2023.01.01",
            clapCount = 999,
            viewCount = 999
        )
    }
}
