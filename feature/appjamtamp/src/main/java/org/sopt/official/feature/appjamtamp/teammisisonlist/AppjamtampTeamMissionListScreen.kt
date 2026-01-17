/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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
import dev.zacsweers.metrox.viewmodel.metroViewModel
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
    teamNumber: String,
    navigateUp: () -> Unit,
    navigateToMissionDetail: (missionId: Int, missionLevel: Int, title: String, ownerName: String?) -> Unit,
    viewModel: AppjamtampTeamMissionListViewModel = metroViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchAppjamMissions(
            teamNumber = teamNumber
        )
    }

    when (state) {
        is AppjamtampMissionListState.Loading -> {}
        is AppjamtampMissionListState.Success -> {
            AppjamtampTeamMissionListScreen(
                teamName = (state as AppjamtampMissionListState.Success).teamName,
                teamMissionList = (state as AppjamtampMissionListState.Success).teamMissionList.missionList,
                onBackButtonClick = navigateUp,
                onMissionItemClick = { mission ->
                    navigateToMissionDetail(
                        mission.id,
                        mission.level.value,
                        mission.title,
                        mission.ownerName
                    )
                }
            )
        }

        is AppjamtampMissionListState.Failure -> {}
    }
}

@Composable
fun AppjamtampTeamMissionListScreen(
    teamName: String,
    teamMissionList: ImmutableList<AppjamtampMissionUiModel>,
    onBackButtonClick: () -> Unit,
    onMissionItemClick: (AppjamtampMissionUiModel) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            BackButtonHeader(
                title = teamName,
                onBackButtonClick = onBackButtonClick,
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
                onMissionItemClick = onMissionItemClick
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
        AppjamtampTeamMissionListScreen(
            teamName = "하이링구얼",
            teamMissionList = mockMissionList,
            onBackButtonClick = {},
            onMissionItemClick = { _ -> }
        )
    }
}
