package org.sopt.official.feature.sopletter.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.sopletter.R

@Composable
internal fun SopletterMainTopBar(
    generation: Int,
    isDownloadBtnVisible: Boolean,
    onCloseClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_32),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }

            Text(
                text = "${generation}기 솝레터",
                style = SoptTheme.typography.heading18B,
                color = SoptTheme.colors.onSurface10,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (isDownloadBtnVisible) {
                IconButton(onClick = onDownloadClick) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_download_32),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
            }

            IconButton(onClick = onReportClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_alert_32),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }
    }
}
