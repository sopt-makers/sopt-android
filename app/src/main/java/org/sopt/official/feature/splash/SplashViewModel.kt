package org.sopt.official.feature.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.official.data.persistence.SoptDataStore
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val store: SoptDataStore
) : ViewModel() {
    val isEmailVerified: Boolean
        get() = store.isEmailVerified

}
