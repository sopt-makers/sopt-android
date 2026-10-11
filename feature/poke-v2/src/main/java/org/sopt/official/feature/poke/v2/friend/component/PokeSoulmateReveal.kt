package org.sopt.official.feature.poke.v2.friend.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.sopt.official.feature.poke.v2.component.PokeProfileAvatar
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.theme.SoptTheme
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun PokeSoulmateReveal(
    user: PokeUserUiState,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnFinish by rememberUpdatedState(onFinish)

    LaunchedEffect(Unit) {
        delay(REVEAL_DURATION_MILLIS.milliseconds)
        currentOnFinish()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(SoptTheme.colors.bg.dim.default)
            .pointerInput(Unit) { detectTapGestures {} },
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Text(
                text = "${user.anonymousName}님의 정체는...",
                style = SoptTheme.typography.title4,
                color = SoptTheme.colors.fg.neutral.bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp),
            )
        }

        PokeProfileAvatar(
            imageUrl = user.profileImageUrl,
            size = 154.dp,
            strokeColor = SoptTheme.colors.stroke.brand.default,
            onClick = {},
        )

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Text(
                text = "${user.userGeneration}기 ${user.userPart}파트 ${user.userName}",
                style = SoptTheme.typography.title4,
                color = SoptTheme.colors.fg.neutral.bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 56.dp),
            )
        }
    }
}

private const val REVEAL_DURATION_MILLIS = 2_000L

@Preview
@Composable
private fun PokeSoulmateRevealPreview() {
    SoptTheme {
        PokeSoulmateReveal(
            user = PokeUserUiState(
                userId = 1,
                userName = "김솝트",
                anonymousName = "익명의 사자",
                userGeneration = 36,
                userPart = "디자인",
                profileImageUrl = null,
                pokeCount = 11,
                isAnonymous = true,
            ),
            onFinish = {},
        )
    }
}
