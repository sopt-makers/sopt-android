/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.poke.v2.component

import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.mds.theme.SoptTheme

/**
 * 콕 찌르기 관계 변화(친구완성 등) 축하 로띠 오버레이.
 *
 * @param lottieRes      재생할 Lottie raw 리소스.
 * @param message        애니메이션 아래에 표시할 문구(예: "{이름}과 친구가 되었어요!").
 * @param onAnimationEnd [repeatCount] 만큼 재생이 끝났을 때 호출.
 */
@Composable
internal fun PokeCelebrationLottie(
    @RawRes lottieRes: Int,
    message: String,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier,
    repeatCount: Int = 2,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))
    val animatable = rememberLottieAnimatable()

    val currentOnAnimationEnd by rememberUpdatedState(onAnimationEnd)

    LaunchedEffect(composition) {
        val loadedComposition = composition ?: return@LaunchedEffect
        animatable.animate(loadedComposition, iterations = repeatCount)
        currentOnAnimationEnd()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SoptTheme.colors.bg.dim.default)
            .noRippleClickable {},
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { animatable.progress },
                modifier = Modifier.size(width = 108.dp, height = 88.dp),
            )

            Text(
                text = message,
                style = SoptTheme.typography.title5,
                color = SoptTheme.colors.fg.neutral.bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = SoptTheme.spacing.s10),
            )
        }
    }
}
