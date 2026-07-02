package org.sopt.official.feature.sopletter.write

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.write.component.SopletterExplainArea
import org.sopt.official.feature.sopletter.write.component.SopletterWriteButton
import org.sopt.official.feature.sopletter.write.component.SopletterWriteTextBox
import org.sopt.official.feature.sopletter.write.model.SopletterWriteUiState

@Composable
fun SopletterWriteRoute(
    onBackClick: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: SopletterWriteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val textFieldState = remember { TextFieldState() }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToMain()
        }
    }

    SopletterWriteScreen(
        uiState = uiState,
        textFieldState = textFieldState,
        onBackClick = onBackClick,
        onPostClick = viewModel::postSopletter
    )
}

@Composable
private fun SopletterWriteScreen(
    uiState: SopletterWriteUiState,
    textFieldState: TextFieldState,
    onBackClick: () -> Unit,
    onPostClick: () -> Unit,
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
            maxLength = maxLength
        )

        Spacer(modifier = Modifier.weight(1f))

        SopletterWriteButton(
            isEnabled = isButtonEnabled,
            onButtonClick = onPostClick
        )

        Spacer(modifier= Modifier.padding(vertical = 24.dp))
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
            onPostClick = { }
        )
    }
}