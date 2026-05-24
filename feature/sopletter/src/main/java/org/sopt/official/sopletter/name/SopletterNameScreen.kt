package org.sopt.official.sopletter.name

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
import org.sopt.official.sopletter.component.SopletterButton
import org.sopt.official.sopletter.name.component.NameInfoHolder

@Composable
fun NameRoute(
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


    NameScreen(
        state = state
    )
}

@Composable
private fun NameScreen(
    state: NameState,
    navigateToSopletterMain: () -> Unit= {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.onSurface950)
    ) {
        Spacer(modifier = Modifier.weight(20f))

        NameInfoHolder(
            info = state,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(228f))

        SopletterButton(
            buttonText = "${state.generation}기 솝레터 바로가가",
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
private fun NameScreenPreview() {
    SoptTheme {
        NameScreen(
            state = NameState(
                name = "익명의 김솝트",
                generation = 38
            ),
            navigateToSopletterMain = {}
        )
    }
}