package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.OrangeAlpha900
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun BadgeClap(
    myClapCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = OrangeAlpha900)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = "+$myClapCount",
            style = SoptTheme.typography.body13M,
            color = SoptTheme.colors.primary
        )
    }
}

@Preview
@Composable
private fun BadgeClapPreview() {
    SoptTheme {
        BadgeClap(
            myClapCount = 50
        )
    }
}
