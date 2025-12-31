package org.sopt.official.feature.appjamtamp.missionlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray200
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AppjamtampDescription(
    teamName : String,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(9.dp),
            )
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = teamName,
            style = SoptTheme.typography.heading16B,
            color = Gray10,
        )

        Spacer(modifier = Modifier.height(3.dp))

        // Todo : 서버에서 주는 값으로 변경가능성있음 체크하기
        Text(
            text = "내가 앱잼 미션을 인증하면",
            style = SoptTheme.typography.body13M,
            color = Gray200
        )

        Text(
            text = "우리 앱잼팀의 오늘 쌓은 점수와 총 점수에 더해져요!",
            style = SoptTheme.typography.body13M,
            color = Gray200
        )
    }
}

@Preview
@Composable
private fun AppjamtampDescriptionPreview() {
    SoptTheme {
        AppjamtampDescription(
            teamName = "도키"
        )
    }
}