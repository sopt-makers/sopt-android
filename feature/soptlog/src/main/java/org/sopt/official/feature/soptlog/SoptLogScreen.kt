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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.feature.soptlog.component.SoptLogEmptySection
import org.sopt.official.feature.soptlog.component.SoptLogSection
import org.sopt.official.feature.soptlog.component.TodayFortuneBanner
import org.sopt.official.feature.soptlog.component.dialog.SoptLogErrorDialog
import org.sopt.official.feature.soptlog.model.MySoptLogItemType
import org.sopt.official.feature.soptlog.model.SoptLogCategory
import org.sopt.official.feature.soptlog.navigation.SoptLogNavigation
import org.sopt.official.feature.soptlog.state.SoptLogNavigationEvent

@Composable
internal fun SoptLogRoute(
    soptLogNavigation: SoptLogNavigation,
    navigateToFortune: () -> Unit = {},
    viewModel: SoptLogViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getSoptLogInfo()
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is SoptLogNavigationEvent.NavigateToPoke -> {
                    soptLogNavigation.navigateToPoke(
                        url = event.url,
                        isNewPoke = event.isNewPoke,
                        currentDestination = 0,
                        friendType = event.friendType
                    )
                }

                is SoptLogNavigationEvent.NavigateToDeepLink -> {
                    soptLogNavigation.navigateToDeepLink(event.url)
                }
            }
        }
    }

    val soptLogState by viewModel.soptLogInfo.collectAsStateWithLifecycle()
    val soptLogInfo = soptLogState.soptLogInfo
    val todayFortuneText by viewModel.todayFortuneText.collectAsStateWithLifecycle("")
    val tracker = LocalTracker.current

    when {
        soptLogState.isLoading -> {
            // TODO: 로딩 화면
        }

        soptLogState.isError -> {
            SoptLogErrorDialog(onCheckClick = viewModel::getSoptLogInfo)
        }

        else -> {
            with(receiver = soptLogState.soptLogInfo) {
                SoptlogScreen(
                    soptLogInfo = soptLogInfo,
                    todayFortuneText = todayFortuneText,
                    onNavigationClick = viewModel::onNavigationClick,
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
private fun SoptlogScreen(
    soptLogInfo: SoptLogInfo,
    todayFortuneText: String,
    onNavigationClick: (url: String) -> Unit,
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

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_soptlog_title),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(36.dp))

        SoptlogContents {
            if (soptLogInfo.isActive) {
                SoptLogSection(
                    title = "솝탬프 로그",
                    items = MySoptLogItemType.entries.filter { it.category == SoptLogCategory.SOPTAMP }.toImmutableList(),
                    soptLogInfo = soptLogInfo,
                    onItemClick = { type ->
                        if (type.url.isNotEmpty()) {
                            onNavigationClick(type.url)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            // TODO: 운영 서버 콕 찌르기 API 불안정 이슈로 콕 찌르기 로그를 엠티뷰로 표시함.
            // TODO: 해당 이슈 해결되면 엠티뷰 제거하고 원래 SoptLogSection 표시 해야 함.
            SoptLogEmptySection(
                content = "콕찌르기 기능 정비 중입니다.\n곧 사용할 수 있어요!"
            )

            Spacer(modifier = Modifier.height(38.dp))

            TodayFortuneBanner(
                title = todayFortuneText,
                onClick = navigateToFortune
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_soptlog_bottom),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
private fun SoptlogContents(
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
private fun PreviewSoptlogScreen() {
    SoptTheme {
        val soptLogInfo = SoptLogInfo(
            isActive = true,
            isFortuneChecked = false,
            todayFortuneText = "오늘의 운세",
            soptampCount = 0,
            viewCount = 216,
            myClapCount = 209,
            clapCount = 0,
            pokeCount = 216,
            newFriendsPokeCount = 511,
            bestFriendsPokeCount = 421,
            soulmatesPokeCount = 761
        )

        val dummyAppServiceNavigation = object : SoptLogNavigation {
            override fun navigateToDeepLink(url: String) {}
            override fun navigateToPoke(url: String, isNewPoke: Boolean, currentDestination: Int, friendType: String?) {}
        }

        SoptlogScreen(
            soptLogInfo = soptLogInfo,
            todayFortuneText = "오늘의 운세",
            onNavigationClick = { url ->
                dummyAppServiceNavigation.navigateToDeepLink(url)
            }
        )
    }
}
