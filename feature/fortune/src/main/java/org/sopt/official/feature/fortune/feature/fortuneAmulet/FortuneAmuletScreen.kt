package org.sopt.official.feature.fortune.feature.fortuneAmulet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable
import org.sopt.official.designsystem.SoptTheme

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
            text = "어려움을 전부 극복할",
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.onSurface300,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "해결부적이 왔솝",
            style = SoptTheme.typography.heading28B,
            color = SoptTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(34.dp))

        AsyncImage(
            model = "https://sopt.s3.ap-northeast-2.amazonaws.com/official/fortune/amulet.png",
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .height(200.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = SoptTheme.colors.primary,
                    shape = RoundedCornerShape(9999.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "홈으로 돌아가기",
                style = SoptTheme.typography.label18SB,
                color = SoptTheme.colors.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
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
