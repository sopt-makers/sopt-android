package org.sopt.official.feature.appjamtamp.missionlist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.feature.appjamtamp.component.LevelOfMission
import org.sopt.official.feature.appjamtamp.model.Mission
import org.sopt.official.feature.appjamtamp.model.Stamp

@Composable
internal fun MissionComponent(
    mission: Mission,
    onClick: () -> Unit = {}
) {
    val shape = MissionShape.DEFAULT_WAVE
    val stamp = Stamp.findStampByLevel(mission.level)

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .defaultMinSize(160.dp, 200.dp)
                .aspectRatio(0.8f)
                .background(
                    color = SoptTheme.colors.onSurface800,
                    shape = shape,
                )
                .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (mission.isCompleted) {
            Image(
                painter = painterResource(stamp.lottieImage),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1.3f)
                    .padding(horizontal = 12.dp)
            )
        } else {
            LevelOfMission(stamp = stamp, spaceSize = 10.dp)
        }
        Spacer(modifier = Modifier.size((if (mission.isCompleted) 8 else 16).dp))
        TitleOfMission(missionTitle = mission.title)
    }
}

@Composable
private fun TitleOfMission(missionTitle: String) {
    Text(
        text = missionTitle,
        style = SoptTheme.typography.body14M,
        color = SoptTheme.colors.onSurface10,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xff000000)
@Composable
private fun MissionComponentPreview() {
    SoptTheme {
        val previewMission =
            Mission(
                id = 1,
                title = "일이삼사오육칠팔구십일일이삼사오육칠팔구십일",
                level = MissionLevel.of(1),
                isCompleted = true,
            )
        MissionComponent(
            mission = previewMission,
        )
    }
}
