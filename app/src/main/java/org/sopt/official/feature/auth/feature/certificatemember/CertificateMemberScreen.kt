package org.sopt.official.feature.auth.feature.certificatemember

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.sopt.official.R.drawable.ic_auth_certification_error
import org.sopt.official.R.drawable.ic_auth_process
import org.sopt.official.designsystem.Black80
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.Gray80
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.AuthTextWithArrow
import org.sopt.official.feature.auth.component.PhoneCertification
import org.sopt.official.feature.auth.component.AuthTextField
import org.sopt.official.feature.auth.component.CertificationSnackBar

@Composable
fun CertificateMemberScreen() {
    // val authError = viewModel.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val onShowSnackBar: () -> Unit = {
        coroutineScope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
        }
    }

    SnackbarHost(
        hostState = snackBarHostState,
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        CertificationSnackBar()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // todo: 화면 비율 계산
        Spacer(modifier = Modifier.height(72.dp))
        TopBar()
        Spacer(modifier = Modifier.height(44.dp))
        PhoneCertification(
            onPhoneNumberClick = onShowSnackBar,
            textColor = Gray80
        )
        Spacer(modifier = Modifier.height(10.dp))
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            text = "",
            hintText = "인증번호를 입력해 주세요.",
            onTextChange = {}
        )

        if (true) {
            ErrorText(text = "인증번호가 일치하지 않아요.\n번호를 확인한 후 다시 입력해 주세요.")
        }

        if (true) {
            ErrorText(text = "솝트 활동 시 사용한 전화번호가 아니예요.\n인증을 실패하신 경우 하단에서 다른 방법으로 인증할 수 있어요.")
        }

        val isEnable by remember { mutableStateOf(true) }
        //todo: state로 빼기
        AuthButton(
            modifier = Modifier
                .fillMaxWidth(),
//                .border(
//                    color = Gray10,
//                    width = 1.dp,
//                    shape = RoundedCornerShape(10.dp)
//                ),
            paddingVertical = 16.dp,
            paddingHorizontal = 15.dp,
            onClick = {},
            containerColor = Black80,
        ) {
            Column {
                AuthTextWithArrow(
                    text = "전화번호로 SOPT 회원 인증에 실패하셨나요?",
                    textStyle = SoptTheme.typography.body14M
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "전화번호가 바뀌었거나, 전화번호 인증이 어려우신 경우\n추가 정보 인증을 통해 가입을 도와드리고 있어요!",
                    color = Gray60
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AuthButton(
            modifier = Modifier.fillMaxWidth(),
            paddingVertical = 16.dp,
            onClick = {},
            containerColor = Gray10,
            contentColor = Gray950,
            disabledContentColor = Gray60,
            isEnabled = isEnable
        ) {
            Text(
                text = "SOPT 회원 인증 완료",
                style = SoptTheme.typography.body14M
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
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
private fun ErrorText(
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painterResource(id = ic_auth_certification_error),
            contentDescription = "에러 아이콘",
        )
        Text(
            text = text,
            color = Color(0XFFBD372F),
            style = SoptTheme.typography.label12SB
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthCertificationPreview() {
    SoptTheme {
        CertificateMemberScreen()
    }
}
