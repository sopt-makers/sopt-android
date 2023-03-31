/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.mission.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.sopt.official.stamp.R
import org.sopt.official.stamp.config.navigation.MissionNavGraph
import org.sopt.official.stamp.designsystem.component.button.SoptampIconButton
import org.sopt.official.stamp.designsystem.component.dialog.SingleOptionDialog
import org.sopt.official.stamp.designsystem.component.layout.LoadingScreen
import org.sopt.official.stamp.designsystem.component.topappbar.SoptTopAppBar
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.domain.MissionLevel
import org.sopt.official.stamp.domain.error.Error
import org.sopt.official.stamp.domain.model.MissionsFilter
import org.sopt.official.stamp.feature.destinations.MissionDetailScreenDestination
import org.sopt.official.stamp.feature.mission.MissionsState
import org.sopt.stamp.feature.mission.MissionsViewModel
import org.sopt.stamp.feature.mission.model.MissionListUiModel
import org.sopt.stamp.feature.mission.model.MissionNavArgs
import org.sopt.stamp.feature.mission.model.MissionUiModel
import org.sopt.official.stamp.feature.ranking.model.RankerNavArg

@MissionNavGraph
@Destination("mission/user")
@Composable
fun UserMissionListScreen(
    missionsViewModel: MissionsViewModel = hiltViewModel(),
    args: RankerNavArg,
    resultNavigator: ResultBackNavigator<Boolean>,
    navigator: DestinationsNavigator
) {
    val state by missionsViewModel.state.collectAsState()

    missionsViewModel.fetchMissions(
        userId = args.userId,
        filter = MissionsFilter.COMPLETE_MISSION.title
    )

    SoptTheme {
        when (state) {
            MissionsState.Loading -> LoadingScreen()
            is MissionsState.Failure -> {
                when ((state as MissionsState.Failure).error) {
                    Error.NetworkUnavailable -> SingleOptionDialog {
                        missionsViewModel.fetchMissions()
                    }
                }
            }

            is MissionsState.Success -> UserMissionListScreen(
                userId = args.userId,
                userName = args.nickname,
                description = args.description,
                missionListUiModel = (state as MissionsState.Success).missionListUiModel,
                isMe = args.userId == missionsViewModel.userId,
                onMissionItemClick = { item -> navigator.navigate(MissionDetailScreenDestination(item)) },
                onClickBack = { resultNavigator.navigateBack() }
            )
        }
    }
}

@Composable
fun UserMissionListScreen(
    userId: Int,
    userName: String,
    description: String,
    missionListUiModel: MissionListUiModel,
    isMe: Boolean,
    onMissionItemClick: (item: MissionNavArgs) -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            UserMissionListHeader(
                title = userName,
                onClickBack = { onClickBack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 12.dp,
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            DescriptionText(description = description)
            Spacer(modifier = Modifier.size(33.dp))
            MissionsGridComponent(
                missions = missionListUiModel.missionList,
                onMissionItemClick = { onMissionItemClick(it) },
                isMe = isMe,
                userId = userId
            )
        }
    }
}

@Composable
fun DescriptionText(description: String) {
    val descriptionText = if (description.length > 21) {
        StringBuilder(description).insert(21, "\n").toString()
    } else {
        description
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.purple100,
                shape = RoundedCornerShape(9.dp)
            )
            .padding(
                horizontal = 15.dp,
                vertical = 13.dp
            ),
        text = descriptionText,
        textAlign = TextAlign.Center,
        style = SoptTheme.typography.sub1,
        color = SoptTheme.colors.onSurface90
    )
}

@Composable
fun UserMissionListHeader(
    title: String,
    onClickBack: () -> Unit = {}
) {
    SoptTopAppBar(
        title = { MissionListHeaderTitle(title = title) },
        navigationIcon = {
            SoptampIconButton(
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_left),
                onClick = { onClickBack() }
            )
        }
    )
}

@Preview
@Composable
fun PreviewUserMissionListScreen() {
    SoptTheme {
        UserMissionListScreen(
            userName = "hello",
            description = "일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이",
            missionListUiModel = MissionListUiModel(
                title = "",
                missionList = listOf(
                    MissionUiModel(
                        id = 1,
                        title = "세미나 끝나고 뒷풀이 1시까지 달리기",
                        level = MissionLevel.of(3),
                        isCompleted = true
                    ),
                    MissionUiModel(
                        id = 2,
                        title = "세미나 끝나고 뒷풀이 2시까지 달리기",
                        level = MissionLevel.of(1),
                        isCompleted = true
                    ),
                    MissionUiModel(
                        id = 3,
                        title = "세미나 끝나고 뒷풀이 3시까지 달리기",
                        level = MissionLevel.of(2),
                        isCompleted = true
                    ),
                    MissionUiModel(
                        id = 4,
                        title = "세미나 끝나고 뒷풀이 4시까지 달리기",
                        level = MissionLevel.of(3),
                        isCompleted = false
                    ),
                    MissionUiModel(
                        id = 5,
                        title = "세미나 끝나고 뒷풀이 5시까지 달리기",
                        level = MissionLevel.of(1),
                        isCompleted = false
                    ),
                    MissionUiModel(
                        id = 6,
                        title = "세미나 끝나고 뒷풀이 6시까지 달리기",
                        level = MissionLevel.of(2),
                        isCompleted = false
                    ),
                    MissionUiModel(
                        id = 7,
                        title = "세미나 끝나고 뒷풀이 7시까지 달리기",
                        level = MissionLevel.of(3),
                        isCompleted = false
                    ),
                    MissionUiModel(
                        id = 8,
                        title = "세미나 끝나고 뒷풀이 8시까지 달리기",
                        level = MissionLevel.of(1),
                        isCompleted = false
                    ),
                    MissionUiModel(
                        id = 9,
                        title = "세미나 끝나고 뒷풀이 9시까지 달리기",
                        level = MissionLevel.of(2),
                        isCompleted = true
                    )
                )
            ),
            isMe = false,
            userId = 1
        )
    }
}
