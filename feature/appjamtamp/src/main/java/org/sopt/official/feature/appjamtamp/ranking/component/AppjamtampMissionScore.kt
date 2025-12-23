package org.sopt.official.feature.appjamtamp.ranking.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.sopt.official.designsystem.GrayAlpha100
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.appjamtamp.R

@Composable
internal fun TopRankingTeamMission(
    // TODO - 서버 응답 값
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.background(color = SoptTheme.colors.onSurface950)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 146f / 232f)
                .clip(shape = RoundedCornerShape(size = 12.dp))
        ) {
//            TODO - 서버 응답 -> UrlImage 사용
//            UrlImage(
//                url = "",
//                contentDescription = null
//            )

            // TODO 임시 이미지
            Image(
                imageVector = ImageVector.vectorResource(id = org.sopt.official.designsystem.R.drawable.img_fake_red),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = "11분 전",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .background(
                        color = GrayAlpha100,
                        shape = RoundedCornerShape(size = 4.dp)
                    )
                    .padding(all = 4.dp),
                color = SoptTheme.colors.onSurface10,
                style = SoptTheme.typography.label11SB
            )
        }

        Spacer(modifier = Modifier.height(height = 8.dp))

        Text(
            text = "보이지 않는 손",
            color = White,
            style = SoptTheme.typography.heading16B,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(height = 4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(size = 20.dp)
                    .clip(CircleShape)
                    .background(color = SoptTheme.colors.onSurface700),
                contentAlignment = Alignment.Center
            ) {
                if (false) { // TODO - 프로필 이미지 존재 여부
                    AsyncImage(
                        model = "",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.ic_user_profile)
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_user_profile),
                        contentDescription = null,
                        tint = SoptTheme.colors.onSurface500,
                        modifier = Modifier
                            .padding(all = 4.dp)
                    )
                }
            }

            Text(
                text = "노바고은비",
                color = SoptTheme.colors.onSurface300,
                style = SoptTheme.typography.label12SB,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}

@Composable
internal fun TodayScoreRaking(
    // TODO - 서버 응답 값
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 10.dp))
            .background(color = SoptTheme.colors.onSurface900)
            .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_rank_1),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Text(
                text = "노바",
                color = White,
                style = SoptTheme.typography.heading16B,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .padding(start = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(height = 14.dp))

        Text(
            text = "총 3000점",
            color = SoptTheme.colors.onSurface300,
            style = SoptTheme.typography.title14SB,
            modifier = Modifier.align(Alignment.End)
        )

        Text(
            text = "+1000점",
            color = White,
            style = SoptTheme.typography.heading20B,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Preview
@Composable
private fun TopRankingTeamMissionPreview() {
    SoptTheme {
        TopRankingTeamMission()
    }
}

@Preview
@Composable
private fun TodayScoreRakingPreview() {
    SoptTheme {
        TodayScoreRaking()
    }
}
