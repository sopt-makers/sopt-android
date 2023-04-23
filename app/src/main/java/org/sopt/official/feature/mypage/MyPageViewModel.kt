package org.sopt.official.feature.mypage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.sopt.official.domain.entity.UserState
import org.sopt.official.util.wrapper.NullableWrapper
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : ViewModel() {
    val userState = MutableStateFlow(NullableWrapper.none<UserState>())

    fun signOut() {

    }

    fun logOut() {

    }

    fun resetSoptamp() {

    }
}