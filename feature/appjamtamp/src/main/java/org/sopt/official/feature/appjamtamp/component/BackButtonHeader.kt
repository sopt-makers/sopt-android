package org.sopt.official.feature.appjamtamp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
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
internal fun BackButtonHeader(
    title: String,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable RowScope.() -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_back_32),
            contentDescription = null,
            tint = SoptTheme.colors.onSurface10,
            modifier = Modifier.clickable(onClick = onBackButtonClick)
        )

        Text(
            text = title,
            style = SoptTheme.typography.heading18B,
            color = SoptTheme.colors.onSurface10,
            modifier = Modifier.weight(1f)
        )

        trailingIcon()
    }
}

@Preview
@Composable
private fun BackButtonHeaderPreview() {
    SoptTheme {
        BackButtonHeader(
            title = "터닝",
            onBackButtonClick = {}
        )
    }
}
