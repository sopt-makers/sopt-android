package org.sopt.official.feature.sopletter.write

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.component.SopletterScaffold
import org.sopt.official.feature.sopletter.write.component.SopletterExplainArea
import org.sopt.official.feature.sopletter.write.component.SopletterTopbar
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
                            message = sideEffect.message,
                            duration = SnackbarDuration.Short,
                        )
                    }
                    is SopletterWriteSideEffect.NavigateToMain -> {
                        onNavigateToMain()
                        //TODO : 네비게이션 연결
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = SoptTheme.colors.background)
            .padding(horizontal = 20.dp),
    ) {
        SopletterTopbar(onBackClick = onBackClick)

        SopletterExplainArea()

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        SopletterWriteTextBox(
            userName = uiState.writerName,
            state = textFieldState,
            maxLength = maxLength,
            onLimitExceeded = onLimitExceeded
        )

        Spacer(modifier = Modifier.weight(1f))

        SopletterWriteButton(
            isEnabled = isButtonEnabled,
            onButtonClick = onPostClick
        )

        Spacer(modifier = Modifier.padding(vertical = 24.dp))
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