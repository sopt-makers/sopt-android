package org.sopt.official.feature.mypage.soptamp.state

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.launch
import org.sopt.official.common.context.findActivity
import org.sopt.official.common.view.toast
import org.sopt.official.feature.mypage.di.userRepository
import timber.log.Timber

@Composable
fun rememberModifyProfileState(): ModifySoptampProfileUiState {
    var current by rememberRetained { mutableStateOf("") }
    var previous by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = context.findActivity<ComponentActivity>()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        userRepository.getUserInfo()
            .onSuccess {
                current = it.profileMessage
                previous = it.profileMessage
            }.onFailure {
                Timber.e(it)
            }
    }

    return ModifySoptampProfileUiState(
        current = current,
        previous = previous,
        onChangeCurrent = { current = it },
        onUpdate = {
            scope.launch {
                userRepository.updateProfileMessage(current)
                    .onSuccess {
                        keyboardController?.hide()
                        context.toast("한마디가 변경되었습니다")
                        activity?.onBackPressedDispatcher?.onBackPressed()
                    }.onFailure {
                        Timber.e(it)
                    }
            }
        }
    )
}
