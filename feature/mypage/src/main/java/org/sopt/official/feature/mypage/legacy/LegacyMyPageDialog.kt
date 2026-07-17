package org.sopt.official.feature.mypage.legacy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray600
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.mypage.component.MyPageButton

/** Exact visual fallback of the dialog used before the MyPage redesign. */
@Composable
internal fun LegacyMyPageDialog(
    onDismissRequest: () -> Unit,
    title: String,
    subTitle: String,
    negativeText: String,
    positiveText: String,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    onPositiveButtonClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 25.dp)
                .background(
                    color = Gray700,
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(top = 26.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                color = White,
                style = SoptTheme.typography.heading16B,
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = subTitle,
                color = Gray100,
                style = SoptTheme.typography.body14M,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(34.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                MyPageButton(
                    modifier = Modifier.weight(1f),
                    paddingVertical = 9.dp,
                    onClick = onDismissRequest,
                    containerColor = Gray600,
                    contentColor = Gray10,
                ) {
                    Text(
                        text = negativeText,
                        style = SoptTheme.typography.body14M,
                    )
                }
                MyPageButton(
                    modifier = Modifier.weight(1f),
                    paddingVertical = 9.dp,
                    onClick = onPositiveButtonClick,
                ) {
                    Text(
                        text = positiveText,
                        style = SoptTheme.typography.body14M,
                    )
                }
            }
        }
    }
}
