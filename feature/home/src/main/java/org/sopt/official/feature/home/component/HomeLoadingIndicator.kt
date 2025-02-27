package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme.colors

@Composable
internal fun HomeProgressIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = colors.background.copy(alpha = 0.55f)),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(width = 32.dp),
            color = colorScheme.secondary,
            trackColor = colorScheme.surfaceVariant,
            strokeWidth = 4.dp,
        )
    }
}
