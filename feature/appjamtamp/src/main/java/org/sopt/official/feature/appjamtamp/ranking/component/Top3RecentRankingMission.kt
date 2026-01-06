package org.sopt.official.feature.appjamtamp.ranking.component

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
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.ranking.model.Top3RecentRankingUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@Composable
internal fun Top3RecentRankingMission(
    top3RecentRanking: Top3RecentRankingUiModel,
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
            UrlImage(
                url = top3RecentRanking.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = top3RecentRanking.createdAt.toRelativeTime(),
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
            text = top3RecentRanking.teamName,
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
                if (top3RecentRanking.userProfileImage?.isEmpty() == true) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_user_profile),
                        contentDescription = null,
                        tint = SoptTheme.colors.onSurface500,
                        modifier = Modifier
                            .padding(all = 4.dp)
                    )
                } else {
                    AsyncImage(
                        model = top3RecentRanking.userProfileImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.ic_user_profile)
                    )
                }
            }

            Text(
                text = "${top3RecentRanking.teamName}${top3RecentRanking.userName}",
                color = SoptTheme.colors.onSurface300,
                style = SoptTheme.typography.label12SB,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}

/* 업로드 시간 변경 함수
서버 응답 형식 : 2025-10-31T00:00:56
업로드 시간: 상대시간 노출
10분 미만 => 방금 전
11분 전 ~ 59분 전 => 그대로 표기
1시간 ~ 24시간 전 => 그대로 표기
25시간 이후 => 1일 전, 2일 전 ...
 */
private fun String?.toRelativeTime(): String {
    if (this.isNullOrBlank()) return ""

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

    val date = dateFormat .parse(this) ?: return ""
    val currentDate = Date()

    val diffMillis = currentDate.time - date.time
    if (diffMillis < 0) return "방금 전"

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

    return when {
        minutes < 10 -> "방금 전"
        minutes in 10..59 -> "${minutes}분 전"
        hours in 1..23 -> "${hours}시간 전"
        else -> "${days}일 전"
    }
}

@Preview
@Composable
private fun Top3RecentRankingMissionPreview() {
    SoptTheme {
        val mockTop3RecentRankingUiModel = Top3RecentRankingUiModel(
            stampId = 1L,
            missionId = 44L,
            userId = 1073L,
            imageUrl = "",
            createdAt = "2025-10-31T00:00:56",
            userName = "노바고은비",
            userProfileImage = null,
            teamName = "노바",
            teamNumber = "FOURTH"
        )

        Top3RecentRankingMission(top3RecentRanking = mockTop3RecentRankingUiModel)
    }
}
