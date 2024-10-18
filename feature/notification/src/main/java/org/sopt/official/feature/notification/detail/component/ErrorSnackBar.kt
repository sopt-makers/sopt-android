package org.sopt.official.feature.notification.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.notification.R

@Composable
internal fun ErrorSnackBar(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = colors.onSurface10,
                shape = RoundedCornerShape(size = 18.dp),
            )
    ) {
        Spacer(modifier = Modifier.width(width = 16.dp))
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_error),
            tint = Color.Unspecified,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(
            text = message,
            style = typography.title14SB,
            color = colors.onSurface900,
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}

@Preview
@Composable
private fun PokeSnackBarPreview() {
    SoptTheme {
        ErrorSnackBar(
            message = "익명 해제 시, 상대방이 나를 알 수 있어요."
        )
    }
}
