package org.sopt.official.feature.appjamtamp.teammisisonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.feature.appjamtamp.component.BackButtonHeader
import org.sopt.official.feature.appjamtamp.component.MissionsGridComponent
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel
import org.sopt.official.feature.appjamtamp.teammisisonlist.model.AppjamtampMissionListState

@Composable
fun AppjamtampTeamMissionListRoute(
    viewModel: AppjamtampTeamMissionListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchAppjamMissions(
            teamNumber = "FIRST" // TODO - 이전 화면에서 네비게이션으로 teamNumber 전달 받아 넣기
        )
    }

    when(state) {
        is AppjamtampMissionListState.Loading -> {}
        is AppjamtampMissionListState.Success -> {
            AppjamtampTeamMissionListScreen(
                teamName = (state as AppjamtampMissionListState.Success).teamName,
                teamMissionList = (state as AppjamtampMissionListState.Success).teamMissionList.missionList
            )
        }
        is AppjamtampMissionListState.Failure -> {}
    }
}

@Composable
internal fun AppjamtampTeamMissionListScreen(
    teamName: String,
    teamMissionList: ImmutableList<AppjamtampMissionUiModel>
) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            BackButtonHeader(
                title = teamName,
                onBackButtonClick = {
                    // Todo : 앱잼 팀 랭킹 화면으로 이동 (뒤로가기)
                },
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 16.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SoptTheme.colors.onSurface950)
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            DescriptionText(
                description = "${teamName}팀이 다같이 인증한 미션",
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.size(size = 16.dp))

            MissionsGridComponent(
                missionList = teamMissionList,
                onMissionItemClick = { item ->
                    // Todo : 미션 상세화면으로 이동
                }
            )
        }
    }
}

@Composable
private fun DescriptionText(
    description: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = description,
        textAlign = TextAlign.Center,
        style = SoptTheme.typography.body16M,
        color = SoptTheme.colors.onSurface50,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(size = 9.dp)
            )
            .padding(vertical = 14.dp)
    )
}

@Preview
@Composable
private fun AppjamtampTeamMissionListScreenPreview() {
    val mockMissionList = persistentListOf(
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

    SoptTheme {
        AppjamtampTeamMissionListScreen(teamName = "하이링구얼", teamMissionList = mockMissionList)
    }
}
