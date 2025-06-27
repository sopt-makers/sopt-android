package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Orange300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage

@Composable
internal fun HomePlaygroundEmptyPost(
    category: String,
    description: String,
    iconUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "아직 최신 글이 없어요"
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = SoptTheme.colors.onSurface900,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 24.dp, end = 18.dp)
            .padding(vertical = 29.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = SoptTheme.typography.body13M,
                color = SoptTheme.colors.onSurface300
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Orange300)) {
                        append(category)
                    }
                    withStyle(SpanStyle(color = SoptTheme.colors.onSurface10)) {
                        append(description)
                    }
                },
                style = SoptTheme.typography.heading16B
            )
        }

        Spacer(
            modifier = Modifier
                .width(18.dp)
        )

        UrlImage(
            url = iconUrl,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    color = SoptTheme.colors.onSurface700
                )
                .padding(15.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePlaygroundEmptyPostPreview() {
    SoptTheme {
        HomePlaygroundEmptyPost(
            category = "[자유]",
            description = "에 오늘의 TMI 적어봐!",
            iconUrl = "https://avatars.githubusercontent.com/u/98209004?v=4",
            onClick = { }
        )
    }
}
