package org.sopt.official.feature.appjamtamp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun AppjamtampButton(
    text: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.primary,
                shape = RoundedCornerShape(9.dp),
            )
            .clickable(onClick = onClicked),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = text,
            style = SoptTheme.typography.heading18B,
            color = SoptTheme.colors.onSurface
        )
    }
}
