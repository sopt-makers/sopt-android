package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun ClapFeedbackHolder(
    clapCount: Int,
    myClapCount: Int?,
    isBadgeVisible: Boolean,
    onPressClap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(bottom = 30.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        ClapButton(
            clapCount = clapCount,
            myClapCount = myClapCount,
            onClicked = onPressClap
        )

        AnimatedVisibility(
            visible = isBadgeVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 }
            ) + fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .padding(bottom = 64.dp)
        ) {
            BadgeClap(myClapCount = myClapCount ?: 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClapFeedbackHolderPreview() {
    SoptTheme {
        ClapFeedbackHolder(
            clapCount = 10,
            myClapCount = 5,
            isBadgeVisible = true,
            onPressClap = {}
        )
    }
}
