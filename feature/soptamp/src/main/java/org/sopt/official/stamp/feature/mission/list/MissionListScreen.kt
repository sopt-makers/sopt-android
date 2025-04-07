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

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.domain.soptamp.error.Error
import org.sopt.official.domain.soptamp.model.MissionsFilter
import org.sopt.official.stamp.R
import org.sopt.official.stamp.config.navigation.MissionNavGraph
import org.sopt.official.stamp.designsystem.component.button.SoptampIconButton
import org.sopt.official.stamp.designsystem.component.button.SoptampSegmentedFloatingButton
import org.sopt.official.stamp.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.stamp.designsystem.component.layout.LoadingScreen
import org.sopt.official.stamp.designsystem.component.mission.MissionComponent
import org.sopt.official.stamp.designsystem.component.topappbar.SoptTopAppBar
import org.sopt.official.stamp.feature.destinations.MissionDetailScreenDestination
import org.sopt.official.stamp.feature.destinations.OnboardingScreenDestination
import org.sopt.official.stamp.feature.destinations.PartRankingScreenDestination
import org.sopt.official.stamp.feature.destinations.RankingScreenDestination
import org.sopt.official.stamp.feature.mission.MissionsState
import org.sopt.official.stamp.feature.mission.MissionsViewModel
import org.sopt.official.stamp.feature.mission.model.MissionListUiModel
import org.sopt.official.stamp.feature.mission.model.MissionNavArgs
import org.sopt.official.stamp.feature.mission.model.MissionUiModel
import org.sopt.official.stamp.feature.mission.model.toArgs
import org.sopt.official.webview.view.WebViewActivity

@MissionNavGraph(true)
@Destination("list")
@Composable
fun MissionListScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<MissionDetailScreenDestination, Boolean>,
    missionsViewModel: MissionsViewModel = hiltViewModel(),
) {
    val state by missionsViewModel.state.collectAsStateWithLifecycle()
    val generation by missionsViewModel.generation.collectAsStateWithLifecycle()
    val nickname by missionsViewModel.nickname.collectAsStateWithLifecycle()
    val reportUrl by missionsViewModel.reportUrl.collectAsStateWithLifecycle()

    val context = LocalContext.current

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> Unit
            is NavResult.Value -> {
                if (result.value) missionsViewModel.fetchMissions()
            }
        }
    }

    LaunchedEffect(Unit) {
        missionsViewModel.fetchMissions()
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

        is MissionsState.Success -> MissionListScreen(
            nickname = nickname,
            generation = generation,
            missionListUiModel = (state as MissionsState.Success).missionListUiModel,
            menuTexts = MissionsFilter.getTitleOfMissionsList().toImmutableList(),
            onMenuClick = { filter -> missionsViewModel.fetchMissions(filter = filter) },
            onMissionItemClick = { item ->
                navigator.navigate(
                    MissionDetailScreenDestination(item)
                )
            },
            onPartRankingButtonClick = { navigator.navigate(PartRankingScreenDestination) },
            onCurrentRankingButtonClick = { navigator.navigate(RankingScreenDestination("${generation}기")) },
            onReportButtonClick = {
                Intent(context, WebViewActivity::class.java).apply {
                    putExtra(
                        WebViewActivity.INTENT_URL,
                        reportUrl
                    )
                    context.startActivity(this)
                }
            },
            onOnboardingButtonClick = { navigator.navigate(OnboardingScreenDestination) }
        )
    }

}

@Composable
fun MissionListScreen(
    nickname: String,
    generation: Int,
    missionListUiModel: MissionListUiModel,
    menuTexts: ImmutableList<String>,
    onMenuClick: (String) -> Unit = {},
    onMissionItemClick: (item: MissionNavArgs) -> Unit = {},
    onPartRankingButtonClick: () -> Unit = {},
    onCurrentRankingButtonClick: () -> Unit = {},
    onReportButtonClick: () -> Unit = {},
    onOnboardingButtonClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MissionListHeader(
                title = missionListUiModel.title,
                menuTexts = menuTexts,
                onMenuClick = onMenuClick,
                onReportButtonClick = onReportButtonClick,
                onOnboardingButtonClick = onOnboardingButtonClick
            )
        },
        floatingActionButton = {
            SoptampSegmentedFloatingButton(
                option1 = "${generation}기 랭킹",
                option2 = "파트별 랭킹",
                modifier = Modifier.padding(horizontal = 54.dp),
                onClickFirstOption = onCurrentRankingButtonClick,
                onClickSecondOption = onPartRankingButtonClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center
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
            if (missionListUiModel.missionList.isEmpty()) {
                MissionEmptyScreen(contentText = missionListUiModel.title)
            } else {
                MissionsGridComponent(
                    missions = missionListUiModel.missionList.toImmutableList(),
                    onMissionItemClick = onMissionItemClick,
                    isMe = true,
                    nickname = nickname
                )
            }
        }
    }
}

@Composable
fun MissionsGridComponent(
    missions: ImmutableList<MissionUiModel>,
    onMissionItemClick: (item: MissionNavArgs) -> Unit = {},
    isMe: Boolean = true,
    nickname: String,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(missions) { missionUiModel ->
            MissionComponent(
                mission = missionUiModel,
                onClick = {
                    onMissionItemClick(missionUiModel.toArgs(isMe, nickname))
                }
            )
        }
        item {
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
        }
    }
}

@Composable
fun MissionEmptyScreen(contentText: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.empty_mission),
            contentDescription = "empty mission list"
        )
        Spacer(modifier = Modifier.size(23.dp))
        Text(
            text = "아직 ${contentText}이 없습니다!",
            style = SoptTheme.typography.body16R,
            color = SoptTheme.colors.onSurface500
        )
    }
}

@Composable
fun MissionListHeader(
    title: String,
    menuTexts: ImmutableList<String>,
    onMenuClick: (String) -> Unit = {},
    onReportButtonClick: () -> Unit = {},
    onOnboardingButtonClick: () -> Unit = {},
) {
    var currentText by remember { mutableStateOf(title) }

    SoptTopAppBar(
        title = { MissionListHeaderTitle(title = title) },
        dropDownButton = {
            DropDownMenuButton(
                menuTexts = menuTexts,
                onMenuClick = { selectedMenuText ->
                    currentText = selectedMenuText
                    onMenuClick(selectedMenuText)
                }
            )
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SoptampIconButton(
                    imageVector = Icons.Rounded.WarningAmber,
                    modifier = Modifier.padding(4.dp),
                    tint = SoptTheme.colors.onSurface10,
                    onClick = onReportButtonClick
                )

                Spacer(modifier = Modifier.width(12.dp))

                SoptampIconButton(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_soptamp_guide),
                    tint = SoptTheme.colors.onSurface10,
                    onClick = onOnboardingButtonClick
                )
            }
        }
    )
}

@Composable
fun MissionListHeaderTitle(title: String) {
    Text(
        text = title,
        color = SoptTheme.colors.onSurface10,
        style = SoptTheme.typography.heading18B
    )
}

@Composable
fun DropDownMenuButton(menuTexts: ImmutableList<String>, onMenuClick: (String) -> Unit = {}) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    Box {
        SoptampIconButton(
            imageVector = if (isMenuExpanded) {
                ImageVector.vectorResource(id = R.drawable.up_expand)
            } else {
                ImageVector.vectorResource(id = R.drawable.down_expand)
            },
            onClick = { isMenuExpanded = true },
            tint = SoptTheme.colors.onSurface10
        )
        DropdownMenu(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White
                )
                .padding(vertical = 12.dp),
            expanded = isMenuExpanded,
            offset = DpOffset((-69).dp, 12.dp),
            onDismissRequest = { isMenuExpanded = false }
        ) {
            menuTexts.forEach { menuText ->
                DropdownMenuItem(
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 8.dp
                    ),
                    onClick = {
                        selectedIndex = menuTexts.indexOf(menuText)
                        onMenuClick(menuText)
                        isMenuExpanded = false
                    }
                ) {
                    Text(
                        text = menuText,
                        style = SoptTheme.typography.heading16B,
                        color = if (menuText == menuTexts[selectedIndex]) Color.Black else SoptTheme.colors.onSurface400
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMissionListScreen() {
    val missionListUiModel = MissionListUiModel(
        title = "완료 미션",
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
    )
    SoptTheme {
        MissionListScreen(
            nickname = "Nunu",
            generation = 35,
            missionListUiModel,
            persistentListOf("전체 미션", "완료 미션", "미완료 미션")
        )
    }
}
