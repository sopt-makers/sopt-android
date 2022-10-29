package org.sopt.official.feature.auth

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var email by mutableStateOf("")
        private set

    val isEmailInvalid by derivedStateOf {
        if (email.isNotEmpty()) EMAIL_REGEX.matches(email).not()
        else false
    }

    fun onChangeEmail(text: String) {
        email = text
    }

    companion object {
        private const val EMAIL_REGEX_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)\$"
        private val EMAIL_REGEX = EMAIL_REGEX_PATTERN.toRegex()
    }
}
