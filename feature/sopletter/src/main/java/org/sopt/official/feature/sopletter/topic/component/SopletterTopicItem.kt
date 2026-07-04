package org.sopt.official.feature.sopletter.topic.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.sopletter.R

@Composable
internal fun SopletterTopicItem(
    topicTitle: String,
    onTopicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(12.dp)
            )
            .noRippleClickable(onClick = onTopicClick)
            .padding(
                start = 20.dp,
                end = 14.dp,
                top = 14.dp,
                bottom = 14.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = topicTitle,
            style = SoptTheme.typography.body16M,
            color = White,
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_chevron_right),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Preview
@Composable
private fun SopletterTopicItemPreview() {
    SoptTheme {
        SopletterTopicItem(
            topicTitle = "38기 회고",
            onTopicClick = {}
        )
    }
}