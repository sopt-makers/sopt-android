package org.sopt.official.feature.mypage.mypage.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.google.firebase.messaging.FirebaseMessaging
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.auth.model.UserActiveState
import org.sopt.official.auth.repository.AuthRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import timber.log.Timber

@Composable
fun rememberMyPageUiState(
    userActiveState: UserActiveState,
    authRepository: AuthRepository,
    stampRepository: StampRepository,
    onRestartApp: () -> Unit
): MyPageUiState {
    var userActiveState by rememberRetained { mutableStateOf(userActiveState) }
    var dialogState by rememberRetained { mutableStateOf(MyPageDialogState.CLEAR) }

    val scope = rememberCoroutineScope()

    return MyPageUiState(
        user = userActiveState,
        dialogState = dialogState,
        onEventSink = { action ->
            when (action) {
                is ResetSoptamp -> {
                    scope.launch {
                        stampRepository.deleteAllStamps()
                            .onSuccess { dialogState = MyPageDialogState.CLEAR }
                            .onFailure { Timber.e(it) }
                    }
                }

                is ClearSoptamp -> {
                    dialogState = MyPageDialogState.CLEAR_SOPTAMP
                }

                is Logout -> {
                    scope.launch {
                        runCatching {
                            FirebaseMessaging.getInstance().token.await()
                        }.onSuccess {
                            authRepository.logout(it)
                                .onSuccess {
                                    authRepository.clearLocalData()
                                    onRestartApp()
                                }.onFailure { error ->
                                    Timber.e(error)
                                }
                        }.onFailure {
                            Timber.e(it)
                        }
                    }
                }

                is CloseDialog -> {
                    dialogState = MyPageDialogState.CLEAR
                }
            }
        }
    )
}
