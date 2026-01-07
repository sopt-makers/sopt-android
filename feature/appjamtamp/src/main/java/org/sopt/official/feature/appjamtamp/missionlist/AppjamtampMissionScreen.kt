package org.sopt.official.feature.appjamtamp.missionlist

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.designsystem.SoptTheme
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
    viewModel: AppjamtampMissionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val state by viewModel.state.collectAsStateWithLifecycle()

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
                }
            }
    }

    AppjamtampMissionScreen(
        state = state,
        onReportButtonClick = viewModel::reportButtonClick,
        onEditMessageButtonClick = viewModel::onEditMessageButtonClick,
        onMenuClick = viewModel::updateMissionFilter,
    )
}

@Composable
private fun AppjamtampMissionScreen(
    state: AppjamtampMissionState,
    onMenuClick: (String) -> Unit = {},
    onReportButtonClick: () -> Unit = {},
    onEditMessageButtonClick: () -> Unit = {}
) {
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
                onClick = {
                    // Todo : 랭킹화면으로 이동
                }
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
                onMissionItemClick = { item ->
                    // Todo : 미션 상세화면으로 이동
                }
            )
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
