package org.sopt.official.feature.mypage.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray600
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.mypage.R

@Composable
fun MyPageDialog(
    onDismissRequest: () -> Unit,
    @StringRes title: Int,
    @StringRes subTitle: Int,
    @StringRes negativeText: Int,
    @StringRes positiveText: Int,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
    ),
    onPositiveButtonClick: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .padding(horizontal = 25.dp)
                .background(
                    color = Gray700,
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = stringResource(id = title),
                color = White,
                style = SoptTheme.typography.heading16B
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(id = subTitle),
                color = Gray100,
                style = SoptTheme.typography.body14M,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(34.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp)
            ) {
                MyPageButton(
                    modifier = Modifier.weight(1f),
                    paddingShape = 10.dp,
                    style = SoptTheme.typography.body14M,
                    paddingVertical = 9.dp,
                    text = negativeText,
                    onButtonClick = onDismissRequest,
                    containerColor = Gray600,
                    contentColor = Gray10
                )
                Spacer(modifier = Modifier.width(6.dp))
                MyPageButton(
                    modifier = Modifier.weight(1f),
                    paddingShape = 10.dp,
                    style = SoptTheme.typography.body14M,
                    paddingVertical = 9.dp,
                    text = positiveText,
                    onButtonClick = onPositiveButtonClick,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageDialogPreview() {
    SoptTheme {
        MyPageDialog(
            onDismissRequest = {},
            title = R.string.mypage_alert_soptamp_reset_title,
            subTitle = R.string.mypage_alert_soptamp_reset_subtitle,
            negativeText = R.string.mypage_alert_soptamp_reset_negative,
            positiveText = R.string.mypage_alert_soptamp_reset_positive
        )
    }
}