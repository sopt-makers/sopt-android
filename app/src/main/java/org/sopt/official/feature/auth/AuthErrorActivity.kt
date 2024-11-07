package org.sopt.official.feature.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.R
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.AuthTextWithArrow

@AndroidEntryPoint
class AuthErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                AuthErrorScreen()
            }
        }
    }

    @Composable
    fun AuthErrorScreen(
        loginAgain: () -> Unit = {}
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.ic_auth_error),
                contentDescription = "로그인 오류 이미지"
            )
            Text(
                text = buildAnnotatedString {
                    append("앗! ")
                    withStyle(style = SpanStyle(color = Orange400)) {
                        append("회원 정보")
                    }
                    append("를 찾을 수 없어요.\n")
                    withStyle(style = SoptTheme.typography.title18SB.toSpanStyle()) {
                        append("먼저 회원가입 후, 다시 로그인해주세요.")
                    }
                },
                color = White,
                style = SoptTheme.typography.title24SB,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            AuthButton(
                onClick = loginAgain,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                paddingVertical = 16.dp
            ) {
                Text(
                    text = "다시 로그인하기",
                    style = SoptTheme.typography.label18SB
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextWithArrow(text = "로그인이 안 되나요?")
            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun AuthErrorPreview() {
        SoptTheme {
            AuthErrorScreen()
        }
    }
}
