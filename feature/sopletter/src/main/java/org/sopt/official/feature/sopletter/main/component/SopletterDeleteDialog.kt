package org.sopt.official.feature.sopletter.main.component

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
internal fun SopletterDeleteDialog(
    onDismiss: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TwoButtonDialog(
        onDismiss = onDismiss,
        positiveButtonText = "삭제",
        negativeButtonText = "취소",
        onPositiveClick = onDeleteClick,
        onNegativeClick = onDismiss,
        buttonVerticalPadding = 13.dp,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "솝레터 삭제하기",
                style = SoptTheme.typography.title18SB,
                color = SoptTheme.colors.onSurface10,
            )

            Text(
                text = "해당 솝레터가 영구적으로 삭제되어요.\n그래도 삭제하시겠어요?",
                style = SoptTheme.typography.body14R,
                color = SoptTheme.colors.onSurface100,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 812)
@Composable
private fun SopletterDeleteDialogPreview() {
    SoptTheme {
        SopletterDeleteDialog(
            onDismiss = {},
            onDeleteClick = {},
        )
    }
}
