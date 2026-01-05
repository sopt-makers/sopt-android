package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun ClapFeedbackHolder(
    clapCount: Int,
    myClapCount: Int?,
    onPressClap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isBadgeVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(bottom = 30.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        ClapButton(
            clapCount = clapCount,
            myClapCount = myClapCount,
            onClicked = {
                onPressClap()
                coroutineScope.launch {
                    isBadgeVisible = true
                    delay(500L)
                    isBadgeVisible = false
                }
            },
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
            onPressClap = {}
        )
    }
}
