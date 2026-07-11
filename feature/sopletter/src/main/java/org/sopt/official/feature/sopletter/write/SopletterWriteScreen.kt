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
package org.sopt.official.feature.sopletter.write

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.common.component.SopletterScaffold
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarVisuals
import org.sopt.official.feature.sopletter.write.component.SopletterExplainArea
import org.sopt.official.feature.sopletter.write.component.SopletterWriteButton
import org.sopt.official.feature.sopletter.write.component.SopletterWriteTextBox
import org.sopt.official.feature.sopletter.write.model.SopletterWriteSideEffect
import org.sopt.official.feature.sopletter.write.model.SopletterWriteUiState

@Composable
fun SopletterWriteRoute(
    onBackClick: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: SopletterWriteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val textFieldState = remember { TextFieldState() }
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is SopletterWriteSideEffect.ShowSnackbar -> {
                        snackBarHostState.showSnackbar(
                            SopletterSnackbarVisuals(
                                message = sideEffect.message,
                                type = sideEffect.type
                            )
                        )
                    }
                    is SopletterWriteSideEffect.NavigateToMain -> {
                        onNavigateToMain()
                    }
                }
            }
    }

    SopletterScaffold(snackbarHostState = snackBarHostState) { paddingValues ->
        SopletterWriteScreen(
            uiState = uiState,
            textFieldState = textFieldState,
            onBackClick = onBackClick,
            onPostClick = { viewModel.postSopletter(textFieldState.text.toString()) },
            onLimitExceeded = viewModel::onLimitExceeded,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SopletterWriteScreen(
    uiState: SopletterWriteUiState,
    textFieldState: TextFieldState,
    onBackClick: () -> Unit,
    onPostClick: () -> Unit,
    onLimitExceeded: () -> Unit,
    modifier: Modifier = Modifier,
    maxLength: Int = 350
) {
    val isButtonEnabled by remember {
        derivedStateOf {
            textFieldState.text.isNotEmpty() &&
                textFieldState.text.length <= maxLength &&
                !uiState.isLoading
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SoptTheme.colors.background)
                .imePadding(),
        ) {
            SopletterTopbar(onBackClick = { if (!uiState.isLoading) onBackClick() })

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                SopletterExplainArea()

                Spacer(modifier = Modifier.padding(vertical = 10.dp))

                SopletterWriteTextBox(
                    userName = uiState.writerName,
                    state = textFieldState,
                    maxLength = maxLength,
                    enabled = !uiState.isLoading,
                    onLimitExceeded = onLimitExceeded,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.padding(vertical = 12.dp))
            }

            SopletterWriteButton(
                isEnabled = isButtonEnabled,
                onButtonClick = onPostClick,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.padding(vertical = 24.dp))
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SoptTheme.colors.primary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SopletterWriteScreenPreview() {
    val textFieldState = remember { TextFieldState() }

    SoptTheme {
        SopletterWriteScreen(
            uiState = SopletterWriteUiState(writerName = "익명의 무무"),
            textFieldState = textFieldState,
            onBackClick = { },
            onPostClick = { },
            onLimitExceeded = { }
        )
    }
}