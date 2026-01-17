/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.fortune.feature.fortuneAmulet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zacsweers.metro.viewmodel.compose.metroViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.fortune.component.CircleShapeBorderButton

@Composable
internal fun FortuneAmuletRoute(
    navigateToSoptLog: () -> Unit,
    viewModel: FortuneAmuletViewModel = metroViewModel(),
) {
    val amplitudeTracker = LocalTracker.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    when {
        state.isLoading -> {
            // Loading View
        }

        state.isFailure -> {
            // Error View
        }

        else -> {
            FortuneAmuletScreen(
                description = state.description,
                amuletDescription = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = state.imageColor)) {
                                append(state.name)
                            }
                            withStyle(style = SpanStyle(color = SoptTheme.colors.onBackground)) {
                                append(state.nameSuffix)
                            }
                        },
                        style = SoptTheme.typography.heading28B,
                    )
                },
                imageUrl = state.imageUrl,
                onBackClick = {
                    amplitudeTracker.track(
                        type = EventType.CLICK,
                        name = "done_home",
                    )
                    navigateToSoptLog()
                }
            ).also {
                amplitudeTracker.track(
                    type = EventType.VIEW,
                    name = "soptmadi_charmcard",
                )
            }
        }
    }
}

@Composable
private fun FortuneAmuletScreen(
    description: String,
    amuletDescription: @Composable ColumnScope.() -> Unit,
    imageUrl: String,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = description,
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.onSurface300,
        )

        Spacer(modifier = Modifier.height(2.dp))

        amuletDescription()

        Spacer(modifier = Modifier.height(34.dp))

        UrlImage(
            url = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 33.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        CircleShapeBorderButton(
            content = {
                Text(
                    text = "돌아가기",
                    style = SoptTheme.typography.label18SB,
                    color = SoptTheme.colors.onBackground,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            },
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Preview
@Composable
fun PreviewFortuneAmuletScreen() {
    SoptTheme {
        FortuneAmuletScreen(
            description = "배고픔을 전부 극복할",
            amuletDescription = {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append("포만감")
                        }
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append("이 왔솝")
                        }
                    },
                    style = SoptTheme.typography.heading28B,
                )
            },
            imageUrl = "https://sopt-makers.s3.ap-northeast-2.amazonaws.com/mainpage/makers-app-img/test_fortune_card.png",
            onBackClick = {}
        )
    }
}
