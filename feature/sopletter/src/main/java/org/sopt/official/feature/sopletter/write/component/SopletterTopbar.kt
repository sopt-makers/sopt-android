package org.sopt.official.feature.sopletter.write.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme

@Composable
fun SopletterTopbar(
    onBackClick : () -> Unit ,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = SoptTheme.colors.background)
            .padding(vertical = 17.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = org.sopt.official.sopletter.R.drawable.icon_chevron_left),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable (onClick = onBackClick)
        )

        Text(
            text = "솝레터 작성",
            color = SoptTheme.colors.onSurface10,
            style = SoptTheme.typography.heading18B
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SopletterTopbarPreview() {
    SoptTheme {
        SopletterTopbar(
            onBackClick = { }
        )
    }
}