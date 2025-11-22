/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.soptlog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.feature.soptlog.component.SoptlogSection
import org.sopt.official.feature.soptlog.component.TodayFortuneBanner
import org.sopt.official.feature.soptlog.model.MySoptLogItemType

@Composable
internal fun SoptlogRouteV2(
    // TODO - navigate 솝탬프 완료 미션 이동
    // TODO - navigate 콕 찌르기 이동 - 총 꼭지르기, 친한친구, 단짝 친구, 천생 연분
    navigateToFortune: () -> Unit = {},
    viewModel: SoptLogViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.getSoptLogInfo()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val soptLogState by viewModel.soptLogInfo.collectAsStateWithLifecycle()
    val soptLogInfo = soptLogState.soptLogInfo
    val tracker = LocalTracker.current

    when {
        soptLogState.isLoading -> {
            // TODO: 로딩 화면
        }

        soptLogState.isError -> {
            // TODO: 오류 화면
        }

        else -> {
            with(receiver = soptLogState.soptLogInfo) {
                SoptlogScreenV2(
                    soptLogInfo = soptLogInfo,
                    navigateToFortune = {
                        navigateToFortune()
                        tracker.track(
                            name = "at36_soptlog_soptmadi",
                            type = EventType.CLICK
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoptlogScreenV2(
    soptLogInfo: SoptLogInfo,
    modifier: Modifier = Modifier,
    navigateToFortune: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = SoptTheme.colors.background)
            .verticalScroll(scrollState)
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = SoptTheme.colors.background,
                titleContentColor = SoptTheme.colors.surface
            ),
            title = {
                Text(
                    text = "마이 솝트로그",
                    style = SoptTheme.typography.body16M,
                )
            }
        )

        Spacer(modifier = modifier.height(20.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_soptlog_title),
            contentDescription = "마이 솝트로그 로고",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = modifier.height(36.dp))

        SoptlogContentsV2 {
            if (soptLogInfo.isActive) {
                SoptlogSection(
                    title = "솝탬프 로그",
                    items = persistentListOf(
                        MySoptLogItemType.COMPLETED_MISSION,
                        MySoptLogItemType.VIEW_COUNT,
                        MySoptLogItemType.RECEIVED_CLAP,
                        MySoptLogItemType.SENT_CLAP
                    ),
                    soptLogInfo = soptLogInfo,
                    onItemClick = { type ->
                        when (type) {
                            MySoptLogItemType.COMPLETED_MISSION -> { }
                            else -> {}
                        }
                    }
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            SoptlogSection(
                title = "콕찌르기 로그",
                items = persistentListOf(
                    MySoptLogItemType.TOTAL_POKE,
                    MySoptLogItemType.CLOSE_FRIEND,
                    MySoptLogItemType.BEST_FRIEND,
                    MySoptLogItemType.SOULMATE
                ),
                soptLogInfo = soptLogInfo,
                onItemClick = { type ->
                    when (type) {
                        MySoptLogItemType.TOTAL_POKE -> { }
                        MySoptLogItemType.CLOSE_FRIEND -> TODO("naviagte 친한 친구")
                        MySoptLogItemType.BEST_FRIEND -> TODO("naviagte 단짝 친구")
                        MySoptLogItemType.SOULMATE -> TODO("naviagte 천생 연분")
                        else -> {}
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(38.dp))

        TodayFortuneBanner(
            title = "오늘 내 운세는?",
            onClick = navigateToFortune
        )

        Spacer(modifier = modifier.height(36.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_soptlog_bottom),
            contentDescription = "마이 솝트로그 하단 이미지",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = modifier.height(72.dp))
    }
}

@Composable
fun SoptlogContentsV2(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        content()
    }
}

@Preview
@Composable
private fun PreviewSoptlogScreenV2() {
    SoptTheme {
        val soptLogInfo = SoptLogInfo(
            isActive = true,
            soptampCount = 0,
            viewCount = 216,
            myClapCount = 209,
            clapCount = 0,
            pokeCount = 216,
            newFriendsPokeCount = 511,
            bestFriendsPokeCount = 421,
            soulmatesPokeCount = 761
        )

        SoptlogScreenV2(
            soptLogInfo = soptLogInfo
        )
    }
}
