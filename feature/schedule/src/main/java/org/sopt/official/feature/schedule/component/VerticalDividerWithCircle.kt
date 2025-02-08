package org.sopt.official.feature.schedule.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun VerticalDividerWithCircle(
    circleColor: Color = SoptTheme.colors.onSurface500,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
    ) {
        VerticalDivider(
            modifier = Modifier
                .height(100.dp)
                .width(1.dp),
            color = SoptTheme.colors.onSurface500
        )
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    circleColor,
                    CircleShape
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerticalDividerWithCirclePreview() {
    SoptTheme {
        VerticalDividerWithCircle()
    }
}
