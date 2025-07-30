package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.TwoButtonDialog

@Composable
internal fun UpdateDialog(
    description: String,
    onDismiss: () -> Unit,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TwoButtonDialog(
        onDismiss = onDismiss,
        positiveButtonText = "업데이트 하기",
        negativeButtonText = "닫기",
        onPositiveClick = onPositiveClick,
        onNegativeClick = onNegativeClick,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "공지사항",
                style = SoptTheme.typography.title18SB,
                color = SoptTheme.colors.primary
            )
            Text(
                text = description,
                style = SoptTheme.typography.body14R,
                color = SoptTheme.colors.onSurface100
            )
        }
    }

}

@Preview(showBackground = true, widthDp = 360, heightDp = 812)
@Composable
private fun UpdateDialogPreview() {
    SoptTheme {
        UpdateDialog(
            description = "안녕하세요, Makers 입니다. SOPT APP이 더 편리한 서비스 경험을 위해 개선되었어요 :)\n" +
                "지금 바로 업데이트를 통해 더 편리하고, 안정적인 SOPT APP을 경험해보세요!\n\n",
            onDismiss = {},
            onPositiveClick = {},
            onNegativeClick = {}
        )
    }
}
