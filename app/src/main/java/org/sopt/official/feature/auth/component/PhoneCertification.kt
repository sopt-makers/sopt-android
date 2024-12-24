package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray80
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme

object AuthCertificationDefaults {
    const val GET_CODE = "인증번호 요청"
    const val CHANGE_CODE = "재전송하기"
}

@Composable
internal fun PhoneCertification(
    modifier: Modifier = Modifier,
    phoneNumber: String = "",
    certificateNumber: String = "",
    onPhoneNumberClick: () -> Unit,
    textColor: Color,
) {
    Column(modifier = modifier) {
        Text(
            text = "전화번호",
            color = textColor,
            style = SoptTheme.typography.body14M
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = phoneNumber,
                hintText = "010-XXXX-XXXX",
                onTextChange = {}
            )
            AuthButton(
                paddingHorizontal = 14.dp,
                paddingVertical = 16.dp,
                onClick = onPhoneNumberClick,
                containerColor = Gray10,
                contentColor = Gray950,
            ) {
                // todo: if(onClick) {CHANGE_CODE}
                Text(
                    text = AuthCertificationDefaults.GET_CODE,
                    style = SoptTheme.typography.body14M
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhoneCertificationPreview() {
    SoptTheme {
        PhoneCertification(
            onPhoneNumberClick = {},
            textColor = Gray80
        )
    }
}