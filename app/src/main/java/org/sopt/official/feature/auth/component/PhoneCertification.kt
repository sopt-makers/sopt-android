package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray80
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme

enum class CertificationButtonText(val message: String) {
    GET_CODE("인증번호 요청"),
    CHANGE_CODE("재전송하기")
}

@Composable
internal fun PhoneCertification(
    modifier: Modifier = Modifier,
    phoneNumber: String = "",
    onPhoneNumberClick: () -> Unit,
    textColor: Color,
) {
    var buttonState by remember { mutableStateOf(CertificationButtonText.GET_CODE) }

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
                // todo: 클릭에 따라 버튼 상태 바뀌게 수정
                Text(
                    text = buttonState.message,
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