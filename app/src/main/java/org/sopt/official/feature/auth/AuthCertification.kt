package org.sopt.official.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R.drawable.ic_auth_process
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray30
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.Gray80
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.PhoneNumberTextField

@Composable
fun AuthCertification(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // todo: 비율 맞추기
        Spacer(modifier = Modifier.weight(1f))
        TopBar()
        Spacer(modifier = Modifier.height(44.dp))
        PhoneCertification()
        Spacer(modifier = Modifier.weight(2f))
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = ic_auth_process),
            contentDescription = "상단 이미지"
        )
        Spacer(modifier = Modifier.height(11.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(66.dp)) {
            Text(
                text = "SOPT 회원인증",
                color = White,
                style = SoptTheme.typography.label12SB
            )
            Text(
                text = "소셜 계정 연동",
                // todo: 색상 문의
                color = Color(0XFF606265),
                style = SoptTheme.typography.label12SB
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "SOPT 회원인증",
            color = Gray10,
            style = SoptTheme.typography.heading24B
        )
        Text(
            text = "Playground는 SOPT 회원만을 위한 공간이에요.\nSOPT 회원인증을 위해 전화번호를 입력해 주세요.",
            color = Gray60,
            style = SoptTheme.typography.label12SB
        )
    }
}

@Composable
fun PhoneCertification(
    modifier: Modifier = Modifier,
    phoneNumber: String = ""
) {
    //todo: state로 빼기
    val isEnable by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "전화번호",
            color = Gray80,
            style = SoptTheme.typography.body14M
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PhoneNumberTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = phoneNumber,
                onTextChange = {}
            )
            AuthButton(
                paddingHorizontal = 14.dp,
                paddingVertical = 16.dp,
                onClick = {},
                containerColor = Gray10,
                contentColor = Gray950,
                disabledContentColor = Gray30,
                isEnabled = isEnable
            ) {
                Text(
                    text = "인증번호 받기",
                    style = SoptTheme.typography.body14M
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthCertificationPreview() {
    SoptTheme {
        AuthCertification()
    }
}