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
package org.sopt.official.stamp.feature.setting.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.sopt.official.stamp.config.navigation.SettingNavGraph
import org.sopt.official.stamp.designsystem.component.layout.SoptColumn
import org.sopt.official.stamp.designsystem.component.toolbar.Toolbar
import org.sopt.official.stamp.designsystem.style.Gray50
import org.sopt.official.stamp.designsystem.style.Purple300
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.domain.fake.FakeUserRepository
import org.sopt.official.stamp.domain.usecase.user.UpdateProfileUseCase
import org.sopt.official.stamp.util.DefaultPreview
import org.sopt.official.stamp.util.addFocusCleaner

@SettingNavGraph
@Destination("introduction")
@Composable
fun UpdateProfileScreen(
    resultNavigator: ResultBackNavigator<Boolean>,
    viewModel: UpdateProfileViewModel = hiltViewModel()
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val introduction by viewModel.introduction.collectAsState("")
    val isFocused by viewModel.isFocused.collectAsState(false)
    val isSuccess by viewModel.isSuccess.collectAsState(false)
    val error by viewModel.error.collectAsState(null)
    val textFieldModifier = remember(isFocused) {
        val modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .padding(14.dp)
            .defaultMinSize(minHeight = 132.dp)
            .clip(RoundedCornerShape(10.dp))
            .onFocusChanged {
                viewModel.onUpdateFocusState(it.isFocused)
            }

        if (!isFocused) {
            modifier
        } else {
            modifier.border(
                width = 1.dp,
                color = Purple300,
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
    val backgroundColor = remember(isFocused) {
        if (isFocused) Color.White else Gray50
    }
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            resultNavigator.navigateBack(true)
        }
    }
    LaunchedEffect(error) {
        if (error != null) {
            Toast.makeText(
                context,
                "요청 시 에러가 발생했습니다. 재시도 해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    SoptTheme {
        SoptColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SoptTheme.colors.white)
                .addFocusCleaner(focusManager) {
                    viewModel.onUpdateFocusState(false)
                }
        ) {
            Toolbar(
                modifier = Modifier.padding(bottom = 10.dp),
                title = {
                    Text(
                        text = "한 마디 편집",
                        style = SoptTheme.typography.h2,
                        modifier = Modifier.padding(start = 4.dp),
                        color = SoptTheme.colors.onSurface
                    )
                },
                onBack = { resultNavigator.navigateBack() }
            )
            TextField(
                value = introduction,
                onValueChange = { viewModel.onIntroductionChanged(it) },
                modifier = textFieldModifier,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    textColor = SoptTheme.colors.onSurface90,
                    placeholderColor = SoptTheme.colors.onSurface60
                ),
                textStyle = SoptTheme.typography.caption1,
                placeholder = {
                    Text(
                        text = "자기 소개를 적어주세요",
                        style = SoptTheme.typography.caption1
                    )
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.onSubmit() },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = introduction.isNotBlank(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SoptTheme.colors.purple300,
                    disabledBackgroundColor = SoptTheme.colors.purple200
                ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(
                    text = "저장",
                    style = SoptTheme.typography.h2,
                    color = SoptTheme.colors.white
                )
            }
        }
    }
}

@DefaultPreview
@Composable
private fun EditIntroductionScreenPreview() {
    UpdateProfileScreen(
        resultNavigator = EmptyResultBackNavigator(),
        viewModel = UpdateProfileViewModel(
            UpdateProfileUseCase(
                FakeUserRepository
            )
        )
    )
}
