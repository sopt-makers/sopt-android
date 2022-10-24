package org.sopt.official.feature.notice.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.designsystem.style.SoptTheme

@Composable
fun NoticeInformationText(
    creator: String,
    createdAt: String
) {
    Text(
        text = "$creator · $createdAt",
        style = SoptTheme.typography.caption,
        color = SoptTheme.colors.onSurface40
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNoticeInformationText() {
    SoptTheme() {
        NoticeInformationText(
            creator = "작성자",
            createdAt = "00.00.00"
        )
    }
}
