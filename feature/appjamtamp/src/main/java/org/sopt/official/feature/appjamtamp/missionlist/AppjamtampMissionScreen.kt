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
package org.sopt.official.feature.appjamtamp.missionlist

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zacsweers.metro.viewmodel.compose.metroViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.OneButtonDialog
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.feature.appjamtamp.component.MissionsGridComponent
import org.sopt.official.feature.appjamtamp.missionlist.component.AppjamtampDescription
import org.sopt.official.feature.appjamtamp.missionlist.component.AppjamtampFloatingButton
import org.sopt.official.feature.appjamtamp.missionlist.component.DropDownHeader
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionListUiModel
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel
import org.sopt.official.feature.appjamtamp.missionlist.state.AppjamtampMissionState
import org.sopt.official.feature.appjamtamp.missionlist.state.AppjamtampSideEffect
import org.sopt.official.feature.appjamtamp.model.MissionFilter
import org.sopt.official.model.UserStatus
import org.sopt.official.webview.view.WebViewActivity

@Composable
internal fun AppjamtampMissionRoute(
    navigateToMissionDetail: (missionId: Int, missionLevel: Int, title: String, ownerName: String?) -> Unit,
    navigateToRanking: () -> Unit,
    viewModel: AppjamtampMissionViewModel = metroViewModel()
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchAppjamMissions(state.currentMissionFilter.isCompleted)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.flowWithLifecycle(lifeCycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    AppjamtampSideEffect.NavigateToWebView -> {
                        Intent(context, WebViewActivity::class.java).apply {
                            putExtra(
                                WebViewActivity.INTENT_URL,
                                state.reportUrl,
                            )
                            context.startActivity(this)
                        }
                    }

                    AppjamtampSideEffect.NavigateToEdit -> {
                        val intent = DeepLinkType.MY_PAGE_SOPTAMP.getIntent(
                            context = context,
                            userStatus = UserStatus.UNAUTHENTICATED, // Todo : 유저 상태에 맞게 변경하기
                            deepLink = DeepLinkType.MY_PAGE_SOPTAMP.link
                        )
                        context.startActivity(intent)
                    }

                    is AppjamtampSideEffect.NavigateToMissionDetail -> {
                        navigateToMissionDetail(
                            sideEffect.mission.id,
                            sideEffect.mission.level.value,
                            sideEffect.mission.title,
                            sideEffect.mission.ownerName
                        )
                    }

                    AppjamtampSideEffect.NavigateToRanking -> navigateToRanking()
                }
            }
    }

    AppjamtampMissionScreen(
        state = state,
        onReportButtonClick = viewModel::reportButtonClick,
        onEditMessageButtonClick = viewModel::onEditMessageButtonClick,
        onMenuClick = viewModel::updateMissionFilter,
        onMissionClick = viewModel::onMissionItemClick,
        onFloatingButtonClick = viewModel::onFloatingButtonClick
    )
}

@Composable
private fun AppjamtampMissionScreen(
    state: AppjamtampMissionState,
    onMenuClick: (String) -> Unit = {},
    onReportButtonClick: () -> Unit = {},
    onEditMessageButtonClick: () -> Unit = {},
    onMissionClick: (mission: AppjamtampMissionUiModel) -> Unit = {},
    onFloatingButtonClick: () -> Unit = {}
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        containerColor = SoptTheme.colors.onSurface950,
        topBar = {
            DropDownHeader(
                title = state.currentMissionFilter.text,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                onMenuClick = onMenuClick,
                onReportButtonClick = onReportButtonClick,
                onEditMessageButtonClick = onEditMessageButtonClick
            )
        },
        floatingActionButton = {
            AppjamtampFloatingButton(
                onClick = onFloatingButtonClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            if (state.teamName.isNotBlank()) {
                AppjamtampDescription(
                    teamName = state.teamName,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            MissionsGridComponent(
                missionList = state.missionList.missionList,
                onMissionItemClick = {
                    if (state.teamName.isNotBlank()) {
                        onMissionClick(it)
                    } else {
                        isDialogVisible = true
                    }
                }
            )
        }
    }

    if (isDialogVisible) {
        OneButtonDialog(
            onDismiss = { isDialogVisible = false },
            buttonText = "확인",
            onButtonClick = { isDialogVisible = false }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "솝탬프 안내",
                    style = SoptTheme.typography.title18SB,
                    color = SoptTheme.colors.onSurface10
                )

                Text(
                    text = "각 미션의 인증 내용은 개인, 앱잼팀 랭킹에서 확인해주세요.",
                    style = SoptTheme.typography.body14R,
                    color = SoptTheme.colors.onSurface100
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppjamtampMissionScreenPreview() {
    SoptTheme {
        val mockMissions = persistentListOf(
            AppjamtampMissionUiModel(
                id = 1,
                title = "세미나 끝나고 뒷풀이 1시까지 달리기",
                level = MissionLevel.of(3),
                isCompleted = true,
            ),
            AppjamtampMissionUiModel(
                id = 2,
                title = "세미나 끝나고 뒷풀이 2시까지 달리기",
                level = MissionLevel.of(1),
                isCompleted = true,
            ),
            AppjamtampMissionUiModel(
                id = 3,
                title = "세미나 끝나고 뒷풀이 3시까지 달리기",
                level = MissionLevel.of(2),
                isCompleted = true,
            ),
            AppjamtampMissionUiModel(
                id = 4,
                title = "세미나 끝나고 뒷풀이 4시까지 달리기",
                level = MissionLevel.of(10),
                isCompleted = true,
            ),
            AppjamtampMissionUiModel(
                id = 5,
                title = "세미나 끝나고 뒷풀이 5시까지 달리기",
                level = MissionLevel.of(1),
                isCompleted = false,
            ),
            AppjamtampMissionUiModel(
                id = 6,
                title = "세미나 끝나고 뒷풀이 6시까지 달리기",
                level = MissionLevel.of(2),
                isCompleted = false,
            ),
        )

        val mockState = AppjamtampMissionState(
            teamName = "도키",
            missionList = AppjamtampMissionListUiModel(
                teamNumber = "1",
                teamName = "도키",
                missionList = mockMissions
            ),
            currentMissionFilter = MissionFilter.ALL
        )

        AppjamtampMissionScreen(
            state = mockState
        )
    }
}
