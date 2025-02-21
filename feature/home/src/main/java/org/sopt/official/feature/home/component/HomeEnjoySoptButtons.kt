package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography

data class HomeAppService(
    val serviceName: String,
    val displayAlarmBadge: Boolean,
    val alarmBadge: String,
    val iconUrl: String,
    val deepLink: String,
)

@Composable
fun HomeEnjoySoptButtons() {
    Box(
        contentAlignment = TopEnd
    ) {
        HomeButtonCircleBox {
            Text(text = "123")
        }
        HomeButtonCircleBox2()
    }
}

@Preview
@Composable
private fun HomeEnjoySoptButtonsPreview() {
    SoptTheme {
        HomeEnjoySoptButtons()

    }
}


@Composable
private fun HomeButtonCircleBox(
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        content = content,
        contentAlignment = Center,
        modifier = Modifier
            .size(size = 80.dp)
            .background(
                shape = CircleShape,
                color = colors.onSurface800,
            ),
    )
}

@Composable
private fun HomeButtonCircleBox2(
) {

    Box(
        contentAlignment = Center,
        modifier = Modifier.background(
            color = Orange400,
            shape = RoundedCornerShape(size = 10.dp),
        ).height(
            height = 20.dp,
        )
    ) {
        Text(
            text = "1+",
            style = typography.label12SB,
            color = colors.background,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
private fun HomeButtonCircleBox2Preview() {
    SoptTheme {
        HomeButtonCircleBox2()
    }
}

@Preview
@Composable
private fun HomeButtonCircleBoxPreview() {
    SoptTheme {
        HomeButtonCircleBox {

        }
    }
}
