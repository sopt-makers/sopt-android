package org.sopt.official.feature.fortune.feature.fortuneAmulet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.component.CircleShapeBorderButton

@Serializable
data object FortuneAmulet

@Composable
internal fun FortuneAmuletRoute(
    paddingValue: PaddingValues,
) {
    FortuneAmuletScreen(
        paddingValue = paddingValue,
    )
}

@Composable
private fun FortuneAmuletScreen(
    paddingValue: PaddingValues,
) {
    Column(
        modifier = Modifier
            .padding(paddingValue)
            .fillMaxSize()
            .background(SoptTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "어려움을 전부 극복할", // 서버에서 받아온 텍스트
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.onSurface300,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = SoptTheme.colors.attention /* 서버에서 받아온 색상 */)) {
                    append("해결부적") // 서버에서 받아온 텍스트
                }
                withStyle(style = SpanStyle(color = SoptTheme.colors.onBackground)) {
                    append("이 왔솝")
                }
            },
            style = SoptTheme.typography.heading28B,
        )
        Spacer(modifier = Modifier.height(34.dp))

        AsyncImage(
            model = "https://어쩌구저쩌구/test_fortune_card.png", // 서버에서 받아온 이미지
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 33.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        CircleShapeBorderButton(
            content = {
                Text(
                    text = "홈으로 돌아가기",
                    style = SoptTheme.typography.label18SB,
                    color = SoptTheme.colors.onBackground,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
        ) {
            // TODO: Navigate to Home
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Preview
@Composable
fun PreviewFortuneAmuletScreen() {
    SoptTheme {
        FortuneAmuletScreen(
            paddingValue = PaddingValues(16.dp),
        )
    }
}
