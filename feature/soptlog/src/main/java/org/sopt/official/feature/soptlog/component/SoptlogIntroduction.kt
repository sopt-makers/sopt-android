package org.sopt.official.feature.soptlog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
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
    onClick: () -> Unit = {},
) {
    val backgroundColor = if (introduction == null) {
        SoptTheme.colors.onSurface700
    } else {
        SoptTheme.colors.onSurface800
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .run {
                if (introduction != null) {
                    clickable {
                        onClick()
                    }
                } else {
                    this
                }
            }
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (introduction == null) {
            IntroductionButton()
        } else {
            IntroductionText(introduction)
        }
    }

}

@Composable
fun IntroductionText(introduction: String) {
    Text(
        text = introduction,
        style = SoptTheme.typography.body14M,
        color = SoptTheme.colors.surface
    )
}

@Composable
fun IntroductionButton() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "한 줄 소개 등록하기",
            style = SoptTheme.typography.label12SB,
            color = SoptTheme.colors.surface
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = SoptTheme.colors.surface,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSoptlogIntroduction() {
    SoptTheme {
        Column {
            SoptlogIntroduction(
                introduction = "이건 null이 아니에요"
            )
            SoptlogIntroduction(
                introduction = null
            )
        }
    }
}
