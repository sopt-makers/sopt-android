/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.setting.withdrawal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.sopt.official.stamp.config.navigation.SettingNavGraph
import org.sopt.official.stamp.designsystem.component.layout.SoptColumn
import org.sopt.official.stamp.designsystem.component.toolbar.Toolbar
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.domain.fake.FakeUserRepository
import org.sopt.official.stamp.domain.usecase.auth.GetUserIdUseCase
import org.sopt.official.stamp.domain.usecase.auth.WithdrawalUseCase
import org.sopt.official.stamp.util.DefaultPreview

@SettingNavGraph
@Destination("withdraw")
@Composable
fun WithdrawalScreen(
    navigator: DestinationsNavigator,
    viewModel: WithdrawalScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isSuccess by viewModel.isSuccess.collectAsState(false)
    val error by viewModel.error.collectAsState(null)

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            ProcessPhoenix.triggerRebirth(context)
        }
    }
    LaunchedEffect(error) {
        if (error != null) {
            Toast.makeText(
                context,
                "서버 요청에 실패했습니다. 다시 시도해 주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    SoptTheme {
        SoptColumn(
            modifier = Modifier.fillMaxSize()
                .background(SoptTheme.colors.white)
        ) {
            Toolbar(
                modifier = Modifier.padding(bottom = 10.dp),
                title = {
                    Text(
                        text = "탈퇴하기",
                        style = SoptTheme.typography.h2,
                        modifier = Modifier.padding(start = 4.dp),
                        color = SoptTheme.colors.onSurface
                    )
                },
                onBack = { navigator.popBackStack() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "탈퇴 시 유의사항",
                style = SoptTheme.typography.sub1,
                color = SoptTheme.colors.onSurface90
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "회원 탈퇴를 신청하시면 해당 이메일은 즉시 탈퇴 처리됩니다.\n\n탈퇴 처리 시 계정 내에서 입력했던 정보 (미션 정보 등)는 영구적으로 삭제되며, 복구가 어렵습니다.",
                style = SoptTheme.typography.caption1
                    .copy(
                        fontSize = 13.sp,
                        lineBreak = LineBreak.Paragraph
                    ),
                color = SoptTheme.colors.onSurface60
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.onPress() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SoptTheme.colors.purple300,
                    disabledBackgroundColor = SoptTheme.colors.purple200
                ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(
                    text = "탈퇴 하기",
                    style = SoptTheme.typography.h2,
                    color = SoptTheme.colors.white
                )
            }
        }
    }
}

@DefaultPreview
@Composable
private fun WithdrawalScreenPreview() {
    WithdrawalScreen(
        navigator = EmptyDestinationsNavigator,
        viewModel = WithdrawalScreenViewModel(
            WithdrawalUseCase(
                FakeUserRepository,
                GetUserIdUseCase(FakeUserRepository)
            )
        )
    )
}
