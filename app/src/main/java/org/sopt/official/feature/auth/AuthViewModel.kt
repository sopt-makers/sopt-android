package org.sopt.official.feature.auth

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.official.domain.usecase.AuthenticateEmailUseCase
import org.sopt.official.feature.auth.model.EmailAuthenticationState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authEmailUseCase: AuthenticateEmailUseCase
) : ViewModel() {
    var email by mutableStateOf("")
        private set

    val isEmailInvalid by derivedStateOf {
        if (email.isNotEmpty()) EMAIL_REGEX.matches(email).not()
        else false
    }

    var emailAuthenticationState by mutableStateOf(EmailAuthenticationState.NONE)
        private set

    fun onChangeEmail(text: String) {
        email = text
    }

    fun onPressAuthenticate() {
        viewModelScope.launch {
            emailAuthenticationState = EmailAuthenticationState.REQUEST
            authEmailUseCase(email, "")
                .fold({
                    emailAuthenticationState = EmailAuthenticationState.SUCCESS
                }, {
                    emailAuthenticationState = EmailAuthenticationState.FAIL
                    Timber.e(it)
                })
        }
    }

    companion object {
        private const val EMAIL_REGEX_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)\$"
        private val EMAIL_REGEX = EMAIL_REGEX_PATTERN.toRegex()
    }
}
