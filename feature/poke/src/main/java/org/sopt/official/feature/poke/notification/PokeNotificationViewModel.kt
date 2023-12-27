package org.sopt.official.feature.poke.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.PokeNotificationList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.use_case.GetPokeNotificationListUseCase
import org.sopt.official.domain.poke.use_case.PokeUserUseCase
import org.sopt.official.feature.poke.UiState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokeNotificationViewModel @Inject constructor(
    private val getPokeNotificationListUseCase: GetPokeNotificationListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {

    private val _pokeNotification = MutableStateFlow<PokeNotificationList?>(PokeNotificationList(arrayListOf(), 0, 0))
    val pokeNotification: StateFlow<PokeNotificationList?> get() = _pokeNotification

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    private var currentPaginationIndex = 0
    private var pokeNotificationJob: Job? = null

    init {
        getPokeNotification()
    }

    fun getPokeNotification() {
        pokeNotificationJob?.let {
            if (it.isActive || !it.isCompleted) return
        }

        pokeNotificationJob = viewModelScope.launch {
            getPokeNotificationListUseCase.invoke(currentPaginationIndex)
                .onSuccess {
                    val oldPokeNotificationList = _pokeNotification.value?.history ?: arrayListOf()
                    _pokeNotification.value = _pokeNotification.value?.copy(
                        history = oldPokeNotificationList.plus(it.history)
                    )
                    currentPaginationIndex++
                }
                .onFailure { Timber.e(it) }
        }
    }

    fun pokeUser(
        userId: Int,
        message: String,
    ) {
        viewModelScope.launch {
            pokeUserUseCase.invoke(
                message = message,
                userId = userId,
            )
                .onSuccess { response ->
                    _pokeUserUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _pokeUserUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _pokeUserUiState.emit(UiState.Failure(throwable))
                }
        }
    }
}