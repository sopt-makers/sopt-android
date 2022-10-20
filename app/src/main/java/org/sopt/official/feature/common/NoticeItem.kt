package org.sopt.official.feature.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Preview(showBackground = true)
@Composable
fun PreviewNoticeItem() {
    SoptTheme {
        NoticeItem()
    }
}
