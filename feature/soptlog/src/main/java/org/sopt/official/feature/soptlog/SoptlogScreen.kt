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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.soptlog.component.DashBoardItem
import org.sopt.official.feature.soptlog.component.EditProfileButton
import org.sopt.official.feature.soptlog.component.SoptlogDashBoard
import org.sopt.official.feature.soptlog.component.SoptlogIntroduction
import org.sopt.official.feature.soptlog.component.SoptlogProfile
import org.sopt.official.feature.soptlog.component.TodayFortuneBanner

@Composable
fun SoptlogRoute(
    navigateToEditProfile: () -> Unit = {},
    navigateToFortune: () -> Unit = {},
    viewModel: SoptLogViewModel = hiltViewModel(),
) {
    val soptLogInfo by viewModel.soptLogInfo.collectAsStateWithLifecycle()
    val amplitudeTracker = LocalTracker.current

    when {
        soptLogInfo.isLoading -> {
            // TODO: 로딩 화면
        }

        soptLogInfo.isError -> {
            // TODO: 오류 화면
        }

        else -> {
            with(soptLogInfo.soptLogInfo) {
                SoptlogScreen(
                    profileImageUrl = profileImageUrl,
                    name = userName,
                    part = part,
                    introduction = profileMessage,
                    dashBoardItems = persistentListOf(
                        DashBoardItem(
                            title = "솝트레벨",
                            icon = R.drawable.ic_sopt_level,
                            content = soptLevel,
                        ),
                        DashBoardItem(
                            title = "콕찌르기",
                            icon = R.drawable.ic_poke_hand,
                            content = pokeCount,
                        ),
                        DashBoardItem(
                            title = if (isActive) "솝템프" else "솝트와",
                            icon = if (isActive) R.drawable.ic_soptamp_hand else R.drawable.ic_calender,
                            content = if (isActive) soptampRank else during,
                        )
                    ),
                    todayFortuneTitle = todayFortuneTitle,
                    navigateToEditProfile = {
                        navigateToEditProfile()
                        amplitudeTracker.track(
                            name = "soptlog_editprofile",
                            type = EventType.CLICK
                        )
                    },
                    navigateToFortune = {
                        navigateToFortune()
                        amplitudeTracker.track(
                            name = "soptlog_soptmadi",
                            type = EventType.CLICK
                        )
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoptlogScreen(
    profileImageUrl: String,
    name: String,
    part: String,
    introduction: String?,
    dashBoardItems: ImmutableList<DashBoardItem>,
    todayFortuneTitle: String,
    modifier: Modifier = Modifier,
    navigateToEditProfile: () -> Unit = {},
    navigateToFortune: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoptTheme.colors.background)
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = SoptTheme.colors.background,
                titleContentColor = SoptTheme.colors.surface
            ),
            title = {
                Text(
                    text = "솝트로그",
                    style = SoptTheme.typography.body16M,
                )
            }
        )

        SoptlogContents {
            SoptlogProfile(
                profileImageUrl = profileImageUrl,
                name = name,
                part = part,
            )

            Spacer(modifier = Modifier.height(12.dp))

            SoptlogIntroduction(
                introduction = introduction
            )

            Spacer(modifier = Modifier.height(12.dp))

            SoptlogDashBoard(
                dashBoardItems = dashBoardItems
            )

            Spacer(modifier = Modifier.height(20.dp))

            EditProfileButton(
                onClick = navigateToEditProfile
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        TodayFortuneBanner(
            title = todayFortuneTitle,
            onClick = navigateToFortune
        )
    }
}

@Composable
fun SoptlogContents(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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
fun PreviewSoptlogScreen() {
    SoptTheme {
        SoptlogScreen(
            profileImageUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo.png",
            name = "차은우",
            part = "안드로이드",
            introduction = "자기소개는 15글자까지",
            dashBoardItems = persistentListOf(
                DashBoardItem(
                    title = "솝트레벨",
                    icon = R.drawable.ic_sopt_level,
                    content = "Lv.6",
                ),
                DashBoardItem(
                    title = "콕찌르기",
                    icon = R.drawable.ic_poke_hand,
                    content = "208회",
                ),
                DashBoardItem(
                    title = "솝트와",
                    icon = R.drawable.ic_calender,
                    content = "33개월",
                )
            ),
            todayFortuneTitle = "오늘 내 운세는?"
        )
    }
}
