package org.sopt.official.feature.mypage.soptamp.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.takahirom.rin.rememberRetained
import org.sopt.official.feature.mypage.di.userRepository
import timber.log.Timber

@Composable
fun rememberModifyProfileState(): ModifySoptampProfileUiState {
    var current by rememberRetained { mutableStateOf("") }
    var previous by rememberRetained { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userRepository.getUserInfo()
            .onSuccess {
                current = it.profileMessage
                previous = it.profileMessage
            }.onFailure { Timber.e(it) }
    }

    return ModifySoptampProfileUiState(
        current = current,
        previous = previous,
        onChangeCurrent = { current = it },
        onChangePrevious = { previous = it }
    )
}
