package org.sopt.official.feature.splash

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.style.SoptTheme

@Composable
fun SplashScreen() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoptTheme.colors.onSurface90)
        ) {
            val textAnimationState = remember {
                MutableTransitionState(false)
                    .apply { targetState = true }
            }
            var isTitleAnimationFinished by remember(textAnimationState.currentState) {
                mutableStateOf(false)
            }
            val remainedAnimationState = remember(isTitleAnimationFinished) {
                MutableTransitionState(isTitleAnimationFinished)
                    .apply { targetState = true }
            }
            val density = LocalDensity.current

            LaunchedEffect(textAnimationState.currentState) {
                if (textAnimationState.currentState) isTitleAnimationFinished = true
            }

            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                AnimatedVisibility(
                    visibleState = textAnimationState,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enter = slideInHorizontally(tween(800)) {
                        with(density) { -20.dp.roundToPx() }
                    } + fadeIn(
                        animationSpec = tween(800, easing = FastOutSlowInEasing),
                        initialAlpha = 0.1f
                    ),
                ) {
                    Text(
                        text = "SOPT",
                        style = SoptTheme.typography.h1,
                        color = SoptTheme.colors.background,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
                AnimatedVisibility(
                    visibleState = textAnimationState,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enter = slideInHorizontally(tween(800)) {
                        with(density) { 20.dp.roundToPx() }
                    } + fadeIn(
                        animationSpec = tween(800, easing = FastOutSlowInEasing),
                        initialAlpha = 0.1f
                    ),
                ) {
                    Text(
                        text = "Shout Our Passion Together",
                        style = SoptTheme.typography.b2,
                        color = SoptTheme.colors.onSurface50,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}

@Preview("Splash 화면")
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
