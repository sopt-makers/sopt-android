package org.sopt.official.feature.auth.feature.socialaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Black80
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray30
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.AuthTextField
import org.sopt.official.feature.auth.component.PhoneCertification

@Composable
fun CertificateAccountScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "가입 시 인증했던\n전화번호를 입력해주세요",
            color = Gray10,
            style = SoptTheme.typography.title24SB,
        )
        Spacer(modifier = Modifier.height(36.dp))
        PhoneCertification(
            onPhoneNumberClick = {},
            textColor = Gray30
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "인증번호를 입력해주세요.",
            color = Gray30,
            style = SoptTheme.typography.body14M
        )
        Spacer(modifier = Modifier.height(12.dp))
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            text = "",
            hintText = "인증번호를 입력해 주세요.",
            onTextChange = {}
        )
        Spacer(modifier = Modifier.height(12.dp))
        AuthButton(
            modifier = Modifier.fillMaxWidth(),
            paddingVertical = 16.dp,
            onClick = {},
            containerColor = White,
            contentColor = Black80,
        ) {
            Text(
                text = "인증 완료하기",
                style = SoptTheme.typography.body16M
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SocialAccountPreview() {
    SoptTheme {
        CertificateAccountScreen()
    }
}