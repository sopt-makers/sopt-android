package org.sopt.official.feature.poke.v2.friend.component

import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.mds.theme.SoptTheme

internal enum class PokeRelationChangeType(
    @RawRes val lottieRes: Int,
    val relationText: String,
) {
    BEST_FRIEND(
        lottieRes = R.raw.friendtobestfriend,
        relationText = "단짝친구가"
    ),

    SOULMATE(
        lottieRes = R.raw.bestfriendtosoulmate,
        relationText = "천생연분이"
    ),
}

@Composable
internal fun PokeRelationChangeLottie(
    type: PokeRelationChangeType,
    anonymousName: String,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String? = null,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(type.lottieRes))
    val lottieAnimatable = rememberLottieAnimatable()
    val currentOnAnimationEnd by rememberUpdatedState(onAnimationEnd)

    LaunchedEffect(composition) {
        val loadedComposition = composition ?: return@LaunchedEffect
        lottieAnimatable.animate(composition = loadedComposition)
        currentOnAnimationEnd()
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
                text = "${anonymousName}님과\n${type.relationText} 되었어요!",
                style = SoptTheme.typography.title4,
                color = SoptTheme.colors.fg.neutral.bold,
                textAlign = TextAlign.Center,
            )
        }

        LottieAnimation(
            composition = composition,
            progress = { lottieAnimatable.progress },
            modifier = Modifier.size(200.dp),
        )

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            hint?.let {
                Text(
                    text = it,
                    style = SoptTheme.typography.title4,
                    color = SoptTheme.colors.fg.neutral.bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 34.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PokeRelationChangeLottieBestFriendPreview() {
    SoptTheme {
        PokeRelationChangeLottie(
            type = PokeRelationChangeType.BEST_FRIEND,
            anonymousName = "익명의 사자",
            hint = "36기 디자인파트",
            onAnimationEnd = {},
        )
    }
}

@Preview
@Composable
private fun PokeRelationChangeLottieSoulmatePreview() {
    SoptTheme {
        PokeRelationChangeLottie(
            type = PokeRelationChangeType.SOULMATE,
            anonymousName = "익명의 사자",
            onAnimationEnd = {},
        )
    }
}
