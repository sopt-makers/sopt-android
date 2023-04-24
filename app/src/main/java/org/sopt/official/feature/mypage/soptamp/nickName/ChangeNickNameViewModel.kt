package org.sopt.official.feature.mypage.soptamp.nickName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.UserState
import org.sopt.official.domain.entity.main.MainViewOperationInfo
import org.sopt.official.domain.entity.main.MainViewResult
import org.sopt.official.domain.entity.main.MainViewUserInfo
import org.sopt.official.stamp.domain.repository.StampRepository
import org.sopt.official.stamp.domain.repository.UserRepository
import org.sopt.official.stamp.feature.setting.nickname.UpdateNicknameErrorState
import org.sopt.official.util.wrapper.asNullableWrapper
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChangeNickNameViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val backPressedSignal = MutableStateFlow(false)
    val nickName = MutableStateFlow("")
    val isValidNickName = MutableStateFlow(true)

    init {
        validateNickName()
    }

    fun changeNickName() {
        viewModelScope.launch {
            nickName.collect {
                userRepository.updateNickname(it)
                    .onSuccess {
                        backPressedSignal.value = true
                    }.onFailure {
                        Timber.e(it)
                    }
            }
        }
    }

    fun validateNickName() {
        viewModelScope.launch {
            nickName.collectLatest {
                if (it.isBlank()) isValidNickName.value = true
                else userRepository.checkNickname(it)
                    .onSuccess {
                        isValidNickName.value = true
                    }.onFailure {
                        Timber.e(it)
                        isValidNickName.value = false
                    }
            }
        }
    }
}