/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.sopletter.name

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.common.component.SopletterButton
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.name.component.SopletterNameInfoHolder
import org.sopt.official.sopletter.R

@Composable
fun SopletterNameRoute(
    navigateToHome: () -> Unit,
    viewModel: SopletterNameViewModel = hiltViewModel()
) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.flowWithLifecycle(lifeCycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    NameSideEffect.NavigateToSopletterMain -> { /* Todo : SopltterMain 네비게이션 연결 */}
                }
            }
    }


    SopletterNameScreen(
        state = state,
        navigateToHome = navigateToHome,
    )
}

@Composable
private fun SopletterNameScreen(
    state: NameState,
    navigateToSopletterMain: () -> Unit= {},
    navigateToHome: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.onSurface950)
    ) {
        SopletterTopbar(
            onBackClick = navigateToHome,
            topbarTitle = "",
            iconRes = R.drawable.ic_close_32
        )

        Spacer(modifier = Modifier.weight(20f))

        SopletterNameInfoHolder(
            info = state,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(228f))

        SopletterButton(
            buttonText = "${state.generation}기 솝레터 바로가기",
            onClick = navigateToSopletterMain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(40f))

    }
}

@Preview
@Composable
private fun SopletterNameScreenPreview() {
    SoptTheme {
        SopletterNameScreen(
            state = NameState(
                name = "익명의 김솝트",
                generation = 38
            ),
            navigateToSopletterMain = {}
        )
    }
}
