/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.R.drawable.ic_auth_process_second
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.model.AuthStatus
import org.sopt.official.feature.auth.utils.di.GoogleGraph

@Composable
internal fun SocialAccountRoute(
    status: AuthStatus,
    name: String,
    phone: String,
    navigateToAuthMain: () -> Unit,
    viewModel: SocialAccountViewModel = metroViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val googleLoginManager = remember(context) {
        (context.applicationContext as GoogleGraph).googleLoginManager
    }

    LaunchedEffect(Unit) {
        viewModel.updateInitialState(
            status = status.type,
            name = name,
            phone = phone
        )
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is SocialAccountSideEffect.NavigateToAuthMain -> navigateToAuthMain()
                }
            }
    }

    SocialAccountScreen(
        title = state.title,
        name = state.name,
        onGoogleLoginCLick = {
            scope.launch {
                val idToken = googleLoginManager.getGoogleIdToken(context)
                viewModel.connectSocialAccount(status = status, token = idToken)
            }
        }
    )
}

@Composable
private fun SocialAccountScreen(
    title: String,
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
        TopBar(
            title = title,
            name = name,
        )
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
    title: String,
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
            text = title,
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
            title = "소셜 계정 연동",
            name = "SOPT",
            onGoogleLoginCLick = {}
        )
    }
}
