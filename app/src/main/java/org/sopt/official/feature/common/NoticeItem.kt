package org.sopt.official.feature.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.style.SoptTheme

@Composable
fun NoticeItem() {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 16.dp, bottom = 14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
        }
    }
}

@Composable
private fun NoticeItemTitle(
    title: String,
    isNewNotice: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isNewNotice) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.icon_new),
                contentDescription = "new content icon"
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Text(
            text = title,
            style = SoptTheme.typography.sub2,
            color = SoptTheme.colors.onSurface90,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoticeItem() {
    SoptTheme {
        NoticeItem()
    }
}
