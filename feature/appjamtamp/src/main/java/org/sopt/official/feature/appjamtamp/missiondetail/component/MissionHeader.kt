package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.component.LevelOfMission
import org.sopt.official.feature.appjamtamp.model.Stamp

@Composable
internal fun MissionHeader(
    title: String,
    stamp: Stamp
) {
    val backgroundColor = SoptTheme.colors.onSurface800

    Surface(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp),
            )
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LevelOfMission(
                stamp = stamp,
                spaceSize = 10.dp
            )
            Text(
                text = title,
                style = SoptTheme.typography.body16M,
                color = SoptTheme.colors.primary,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    SoptTheme {
        MissionHeader(
            title = "오늘은 무슨 일을 할까?",
            stamp = Stamp.LEVEL2
        )
    }
}

@Preview
@Composable
private fun HeaderPreview2() {
    SoptTheme {
        MissionHeader(
            title = "오늘은 무슨 일을 할까?",
            stamp = Stamp.LEVEL10
        )
    }
}
