/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.mission.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.domain.soptamp.error.Error
import org.sopt.official.domain.soptamp.model.MissionsFilter
import org.sopt.official.stamp.R
import org.sopt.official.stamp.config.navigation.MissionNavGraph
import org.sopt.official.stamp.designsystem.component.button.SoptampIconButton
import org.sopt.official.stamp.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.stamp.designsystem.component.layout.LoadingScreen
import org.sopt.official.stamp.designsystem.component.topappbar.SoptTopAppBar
import org.sopt.official.stamp.feature.destinations.MissionDetailScreenDestination
import org.sopt.official.stamp.feature.mission.MissionsState
import org.sopt.official.stamp.feature.mission.MissionsViewModel
import org.sopt.official.stamp.feature.mission.model.MissionListUiModel
import org.sopt.official.stamp.feature.mission.model.MissionNavArgs
import org.sopt.official.stamp.feature.mission.model.MissionUiModel
import org.sopt.official.stamp.feature.ranking.model.RankerNavArg

@MissionNavGraph
@Destination("mission/user")
@Composable
fun UserMissionListScreen(
    missionsViewModel: MissionsViewModel = hiltViewModel(),
    args: RankerNavArg,
    resultNavigator: ResultBackNavigator<Boolean>,
    navigator: DestinationsNavigator,
) {
    val state by missionsViewModel.state.collectAsStateWithLifecycle()
    val nickname by missionsViewModel.nickname.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        missionsViewModel.fetchMissions(
            filter = MissionsFilter.COMPLETE_MISSION.title,
            nickname = args.nickname,
        )
    }

    when (state) {
        MissionsState.Loading -> LoadingScreen()
        is MissionsState.Failure -> {
            when ((state as MissionsState.Failure).error) {
                Error.NetworkUnavailable -> NetworkErrorDialog(
                    onRetry = missionsViewModel::fetchMissions
                )
            }
        }

        is MissionsState.Success -> UserMissionListScreen(
            userName = args.nickname,
            description = args.description,
            missionListUiModel = (state as MissionsState.Success).missionListUiModel,
            isMe = args.nickname == nickname,
            onMissionItemClick = { item -> navigator.navigate(MissionDetailScreenDestination(item)) },
            onClickBack = resultNavigator::navigateBack
        )
    }
}


@Composable
fun UserMissionListScreen(
    userName: String,
    description: String,
    missionListUiModel: MissionListUiModel,
    isMe: Boolean,
    onMissionItemClick: (item: MissionNavArgs) -> Unit = {},
    onClickBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            UserMissionListHeader(
                title = userName,
                onClickBack = onClickBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SoptTheme.colors.onSurface950)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            DescriptionText(description = description)
            Spacer(modifier = Modifier.size(33.dp))
            MissionsGridComponent(
                missions = missionListUiModel.missionList.toImmutableList(),
                onMissionItemClick = { onMissionItemClick(it) },
                isMe = isMe,
                nickname = userName
            )
        }
    }
}

@Composable
fun DescriptionText(description: String) {
    val descriptionText = if (description.length > 21) {
        StringBuilder(description).insert(21, "\n").toString()
    } else {
        description.ifBlank { "설정된 한 마디가 없습니다" }
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(9.dp)
            )
            .padding(
                horizontal = 15.dp,
                vertical = 13.dp
            ),
        text = descriptionText,
        textAlign = TextAlign.Center,
        style = SoptTheme.typography.body16M,
        color = SoptTheme.colors.onSurface50
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
                tint = SoptTheme.colors.onSurface10,
                onClick = onClickBack
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
        )
    }
}
