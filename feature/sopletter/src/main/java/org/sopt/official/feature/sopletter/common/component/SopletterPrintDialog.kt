package org.sopt.official.feature.sopletter.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.TwoButtonDialog

@Composable
fun SopletterPrintDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onPrintConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TwoButtonDialog(
        onDismiss = onDismissRequest,
        positiveButtonText = "출력",
        negativeButtonText = "취소",
        onPositiveClick = onPrintConfirm,
        onNegativeClick = onDismissRequest,
        buttonVerticalPadding = 13.dp,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "솝레터 출력하기",
                style = SoptTheme.typography.title18SB,
                color = SoptTheme.colors.onSurface10,
            )

            Text(
                text = "${title}의 모든 메시지가 하나의 이미지로 출력돼요. 솝레터를 출력하여 우리 기수의 이야기를 공유해보세요!",
                style = SoptTheme.typography.body14R,
                color = SoptTheme.colors.onSurface100,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 812)
@Composable
private fun SopletterPrintDialogPreview() {
    SoptTheme {
        SopletterPrintDialog(
            title = "nn기 솝레터",
            onDismissRequest = {},
            onPrintConfirm = {},
        )
    }
}
