package org.sopt.official.feature.mypage.signOut

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val restartSignal = MutableSharedFlow<Unit>()

    fun signOut() {
        viewModelScope.launch {
            authRepository.withdraw()
                .onSuccess {
                    restartSignal.emit(Unit)
                }.onFailure {
                    Timber.e(it)
                }
        }
    }
}