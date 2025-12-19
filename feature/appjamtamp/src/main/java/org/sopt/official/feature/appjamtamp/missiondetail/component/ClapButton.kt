package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R

@Composable
internal fun ClapButton(
    clapCount: Int,
    myClapCount: Int?,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .background(
                color = SoptTheme.colors.onSurface900,
                shape = RoundedCornerShape(32.dp),
            )
            .border(
                width = 1.dp,
                color = SoptTheme.colors.onSurface700,
                shape = RoundedCornerShape(32.dp),
            )
            .clip(RoundedCornerShape(32.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = SoptTheme.colors.primary
                ),
                onClick = onClicked
            )
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_clap),
            contentDescription = null,
            tint = if (myClapCount == 0) SoptTheme.colors.onSurface400 else Color.Unspecified
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = clapCount.toString(),
            style = SoptTheme.typography.heading20B,
            color = SoptTheme.colors.primary
        )
    }
}

@Preview
@Composable
private fun ClapButtonPreview() {
    SoptTheme {
        ClapButton(
            clapCount = 999,
            myClapCount = 0,
            onClicked = {}
        )
    }
}
