package org.sopt.official.feature.appjamtamp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Orange300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.model.Stamp

@Composable
internal fun LevelOfMission(
    stamp: Stamp,
    spaceSize: Dp
) {
    if (stamp.missionLevel == MissionLevel.of(10)) {
        SpecialMissionTitle()
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceSize),
        ) {
            MissionLevelOfStar(stamp = stamp)
        }
    }
}

@Composable
private fun SpecialMissionTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_star_15),
            contentDescription = null,
            tint = Orange300,
        )

        Spacer(modifier = Modifier.width(2.dp))

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_star_close),
            contentDescription = null,
            tint = SoptTheme.colors.primary,
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "10",
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.primary,
        )
        Spacer(modifier = Modifier.width(7.dp))
        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(7.dp),
            color = SoptTheme.colors.onSurface600,
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = "특별미션",
            style = SoptTheme.typography.body14M,
            color = Orange300,
        )
    }
}

@Composable
private fun MissionLevelOfStar(stamp: Stamp) {
    (MissionLevel.MINIMUM_LEVEL..MissionLevel.MAXIMUM_LEVEL).forEach {
        val starColor =
            if (it <= stamp.missionLevel.value) {
                stamp.starColor
            } else {
                Stamp.defaultStarColor
            }
        Icon(
            painter = painterResource(id = R.drawable.ic_star_15),
            contentDescription = null,
            tint = starColor,
        )
    }
}

@Preview
@Composable
private fun LevelOfMissionPreview() {
    SoptTheme {
        LevelOfMission(
            stamp = Stamp.LEVEL3,
            spaceSize = 10.dp,
        )
    }
}
