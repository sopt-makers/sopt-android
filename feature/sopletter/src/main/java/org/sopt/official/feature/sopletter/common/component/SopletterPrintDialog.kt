package org.sopt.official.feature.sopletter.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme

@Composable
fun SopletterPrintDialog(
    generation: Int,
    onDismissRequest: () -> Unit,
    onPrintConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SoptTheme.colors.onSurface800)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "솝레터 출력하기",
                    style = SoptTheme.typography.title16SB,
                    color = SoptTheme.colors.onSurface10
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${generation}기 솝레터의 모든 메세지가 하나의 이미지로\n출력돼요. 솝레터를 출력하여 우리 기수의 이야\n기를 공유해보세요!",
                    style = SoptTheme.typography.body13M,
                    color = SoptTheme.colors.onSurface30,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SopletterDialogButton(
                        text = "취소",
                        textColor = SoptTheme.colors.onSurface10,
                        backgroundColor = SoptTheme.colors.onSurface600,
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f)
                    )

                    SopletterDialogButton(
                        text = "출력",
                        textColor = SoptTheme.colors.background,
                        backgroundColor = SoptTheme.colors.onSurface10,
                        onClick = onPrintConfirm,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SopletterDialogButton(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SoptTheme.typography.label14SB,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SopletterPrintDialogPreview() {
    SoptTheme {
        SopletterPrintDialog(
            generation = 1,
            onDismissRequest = {},
            onPrintConfirm = {}
        )
    }
}