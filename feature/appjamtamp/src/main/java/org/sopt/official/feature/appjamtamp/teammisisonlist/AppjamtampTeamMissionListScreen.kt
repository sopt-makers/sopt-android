package org.sopt.official.feature.appjamtamp.teammisisonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.feature.appjamtamp.component.BackButtonHeader
import org.sopt.official.feature.appjamtamp.missionlist.component.MissionComponent
import org.sopt.official.feature.appjamtamp.model.Mission

@Composable
internal fun AppjamtampTeamMissionListScreen(
    missionList: List<Mission> = emptyList()
) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            BackButtonHeader(
                title = "노바",
                onBackButtonClick = {},
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
                description = "노바팀이 다같이 인증한 미션", // TODO - "${}팀이 다같이 인증한 미션"
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.size(size = 16.dp))

            MissionsGridComponent(missionList = missionList)
        }
    }
}

@Composable
fun DescriptionText(
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

// TODO - 앱잼탬프 홈 화면에서도 사용되는 컴포넌트 -> 추후 이 컴포넌트 삭제하고 공용 컴포넌트 사용해야 함
@Composable
private fun MissionsGridComponent(
    missionList: List<Mission> = emptyList()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        verticalArrangement = Arrangement.spacedBy(
            space = 40.dp,
            alignment = Alignment.Top
        ),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(count = missionList.size) { index ->
            MissionComponent(
                mission = missionList[index],
                onClick = {}
            )
        }
        item {
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
        }
    }
}

@Preview
@Composable
private fun AppjamtampTeamMissionListScreenPreview() {
    val mockMissionList = listOf(
        Mission(
            id = 1,
            title = "세미나 끝나고 뒷풀이 1시까지 달리기",
            level = MissionLevel.of(level = 3),
            isCompleted = true
        ),
        Mission(
            id = 2,
            title = "세미나 끝나고 뒷풀이 2시까지 달리기",
            level = MissionLevel.of(level = 1),
            isCompleted = true
        ),
        Mission(
            id = 3,
            title = "세미나 끝나고 뒷풀이 3시까지 달리기",
            level = MissionLevel.of(level = 2),
            isCompleted = true
        ),
        Mission(
            id = 4,
            title = "세미나 끝나고 뒷풀이 4시까지 달리기",
            level = MissionLevel.of(level = 3),
            isCompleted = false
        ),
        Mission(
            id = 5,
            title = "세미나 끝나고 뒷풀이 5시까지 달리기",
            level = MissionLevel.of(level = 1),
            isCompleted = false
        ),
        Mission(
            id = 6,
            title = "세미나 끝나고 뒷풀이 6시까지 달리기",
            level = MissionLevel.of(level = 2),
            isCompleted = false
        ),
        Mission(
            id = 7,
            title = "세미나 끝나고 뒷풀이 7시까지 달리기",
            level = MissionLevel.of(level = 3),
            isCompleted = false
        ),
        Mission(
            id = 8,
            title = "세미나 끝나고 뒷풀이 8시까지 달리기",
            level = MissionLevel.of(level = 1),
            isCompleted = false
        ),
        Mission(
            id = 9,
            title = "세미나 끝나고 뒷풀이 9시까지 달리기",
            level = MissionLevel.of(level = 2),
            isCompleted = true
        ),
    )

    SoptTheme {
        AppjamtampTeamMissionListScreen(missionList = mockMissionList)
    }
}
