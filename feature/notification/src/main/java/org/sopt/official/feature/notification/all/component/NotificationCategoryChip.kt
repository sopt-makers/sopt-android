package org.sopt.official.feature.notification.all.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun NotificationCategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = category,
        color = if (isSelected) SoptTheme.colors.surface else SoptTheme.colors.onSurface300,
        style = SoptTheme.typography.label14SB,
        modifier = modifier
            .background(
                color = if (isSelected) SoptTheme.colors.onSurface700 else SoptTheme.colors.onSurface800,
                shape = CircleShape
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) SoptTheme.colors.onSurface100 else SoptTheme.colors.onSurface700
                ),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .padding(vertical = 9.dp, horizontal = 14.dp)
    )
}