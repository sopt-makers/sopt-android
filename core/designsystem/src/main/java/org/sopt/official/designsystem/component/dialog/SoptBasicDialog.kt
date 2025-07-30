package org.sopt.official.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.designsystem.SoptTheme

@Composable
fun SoptBasicDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties
    ) {
        Surface(
            color = SoptTheme.colors.onSurface800,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(color = SoptTheme.colors.onSurface800)
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 812)
@Composable
private fun SoptBasicDialogPreview() {
    SoptTheme {
        SoptBasicDialog(
            onDismiss = {}
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "다이얼로그 테스트",
                    style = SoptTheme.typography.title18SB,
                    color = SoptTheme.colors.primary
                )
                Text(
                    text = "다이얼로그 테스트중이에용 어때어때",
                    style = SoptTheme.typography.body14R,
                    color = SoptTheme.colors.onSurface100
                )
            }
        }
    }
}
