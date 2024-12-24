package org.sopt.official.feature.auth.feature.socialaccount

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.auth.component.AuthButton

@Composable
fun ChangeAccountScreen(
    onGoogleLoginCLick : ()-> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "새로 연결할\n소셜 계정을 선택해주세요",
            color = Gray10,
            style = SoptTheme.typography.title24SB,
        )
        Spacer(modifier = Modifier.height(36.dp))
        AuthButton(
            paddingVertical = 13.dp,
            onClick = onGoogleLoginCLick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_auth_google),
                contentDescription = "구글 로고",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Google 계정 연결하기",
                style = SoptTheme.typography.label16SB
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChangeAccountPreview() {
    SoptTheme {
        ChangeAccountScreen(
            onGoogleLoginCLick = {}
        )
    }
}