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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.feature.soptlog.component.SoptlogSection
import org.sopt.official.feature.soptlog.component.TodayFortuneBanner
import org.sopt.official.feature.soptlog.model.MySoptLogItemType
import org.sopt.official.feature.soptlog.navigation.SoptlogNavigation
import org.sopt.official.feature.soptlog.navigation.SoptlogUrl

@Composable
internal fun SoptlogRoute(
    soptlogNavigation: SoptlogNavigation,
    navigateToFortune: () -> Unit = {},
    viewModel: SoptLogViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    LifecycleStartEffect(Unit) {
        // ON_START
        viewModel.getSoptLogInfo()

        onStopOrDispose {}
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
                SoptlogScreen(
                    soptLogInfo = soptLogInfo,
                    onNavigationClick = { url ->
                        val soptlogAppServicesNavigation = soptlogNavigation as SoptlogNavigation.SoptlogAppServiceNavigation

                        when(SoptlogUrl.from(url)) {
                            SoptlogUrl.POKE, SoptlogUrl.POKE_FRIEND_SUMMARY -> {
                                scope.launch {
                                    viewModel.fetchIsNewPoke()
                                        .onSuccess { isNewPoke ->
                                            val uri = url.toUri()
                                            val type = uri.getQueryParameter("type")

                                            soptlogAppServicesNavigation.navigateToPoke(
                                                url = url,
                                                isNewPoke = isNewPoke,
                                                currentDestination = 0,
                                                friendType = type
                                            )
                                        }
                                }
                            }
                            SoptlogUrl.SOPTAMP -> {
                                soptlogAppServicesNavigation.navigateToDeepLink(url)
                            }
                            else -> {}
                        }
                    },
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

        Spacer(modifier = modifier.height(20.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_soptlog_title),
            contentDescription = "마이 솝트로그 로고",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = modifier.height(36.dp))

        SoptlogContents {
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
                            MySoptLogItemType.COMPLETED_MISSION -> { onNavigationClick(type.url) }
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
                        MySoptLogItemType.TOTAL_POKE,
                        MySoptLogItemType.CLOSE_FRIEND,
                        MySoptLogItemType.BEST_FRIEND,
                        MySoptLogItemType.SOULMATE -> { onNavigationClick(type.url) }
                        else -> {}
                    }
                }
            )

            Spacer(modifier = Modifier.height(38.dp))

            TodayFortuneBanner(
                title = "오늘 내 운세는?",
                onClick = navigateToFortune
            )
        }

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
            soptampCount = 0,
            viewCount = 216,
            myClapCount = 209,
            clapCount = 0,
            pokeCount = 216,
            newFriendsPokeCount = 511,
            bestFriendsPokeCount = 421,
            soulmatesPokeCount = 761
        )

        val dummyAppServiceNavigation = object : SoptlogNavigation.SoptlogAppServiceNavigation {
            override fun navigateToDeepLink(url: String) {}
            override fun navigateToPoke(url: String, isNewPoke: Boolean, currentDestination: Int, friendType: String?) {}
        }

        SoptlogScreen(
            soptLogInfo = soptLogInfo,
            onNavigationClick = { url ->
                dummyAppServiceNavigation.navigateToDeepLink(url)
            }
        )
    }
}
