package org.sopt.official.feature.auth.feature.socialaccount

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import org.sopt.official.R
import org.sopt.official.R.drawable.ic_auth_process_second
import org.sopt.official.common.view.toast
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.model.AuthStatus

@Composable
internal fun SocialAccountRoute(
    status: AuthStatus,
    name: String,
    onGoogleLoginCLick: () -> Unit,
    viewModel: SocialAccountViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is SocialAccountSideEffect.ShowToast -> context.toast(sideEffect.message)
                }
            }
    }

    SocialAccountScreen(
        name = name,
        onGoogleLoginCLick = {
            viewModel.connectSocialAccount(status)
        }
    )
}

@Composable
private fun SocialAccountScreen(
    name: String,
    onGoogleLoginCLick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        TopBar(name)
        Spacer(modifier = Modifier.height(66.dp))
        AuthButton(
            padding = PaddingValues(vertical = 12.dp),
            onClick = onGoogleLoginCLick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_auth_google),
                contentDescription = "구글 로고",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Google로 로그인",
                style = SoptTheme.typography.label16SB
            )
        }
    }
}

@Composable
private fun TopBar(
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = ic_auth_process_second),
            contentDescription = "상단 이미지"
        )
        Spacer(modifier = Modifier.height(11.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(66.dp)) {
            Text(
                text = "SOPT 회원인증",
                color = Gray100,
                style = SoptTheme.typography.label12SB
            )
            Text(
                text = "소셜 계정 연동",
                color = White,
                style = SoptTheme.typography.label12SB
            )
        }
        Spacer(modifier = Modifier.height(54.dp))
        Text(
            text = "소셜 계정연동",
            color = Gray10,
            style = SoptTheme.typography.heading24B
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "반갑습니다 ${name}님\n소셜로그인을 진행하여 회원가입을 완료해주세요",
            color = Gray60,
            style = SoptTheme.typography.label12SB,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChangeAccountPreview() {
    SoptTheme {
        SocialAccountScreen(
            name = "SOPT",
            onGoogleLoginCLick = {}
        )
    }
}