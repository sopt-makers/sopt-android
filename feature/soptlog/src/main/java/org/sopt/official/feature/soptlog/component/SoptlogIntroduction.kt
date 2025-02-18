package org.sopt.official.feature.soptlog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun SoptlogIntroduction(
    introduction: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                SoptTheme.colors.onSurface800
            )
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        IntroductionText(
            introduction = if (introduction.isNullOrBlank()) "프로필 수정에서 한 줄 소개 등록해보세요!" else introduction
        )
    }

}

@Composable
fun IntroductionText(introduction: String) {
    Text(
        text = introduction,
        style = SoptTheme.typography.body14M,
        color = SoptTheme.colors.onSurface100
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSoptlogIntroduction() {
    SoptTheme {
        Column {
            SoptlogIntroduction(
                introduction = "이건 null이 아니에요",
            )
            SoptlogIntroduction(
                introduction = null,
            )
        }
    }
}
