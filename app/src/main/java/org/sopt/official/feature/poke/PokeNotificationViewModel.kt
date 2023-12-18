package org.sopt.official.feature.poke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.data.model.poke.response.PokeNotificationResponse
import org.sopt.official.domain.usecase.poke.GetPokeNotificationUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokeNotificationViewModel @Inject constructor(
    private val getPokeNotificationUseCase: GetPokeNotificationUseCase
) : ViewModel() {
    private val _pokeNotification = MutableStateFlow<PokeNotificationResponse?>(null)
    val pokeNotification: StateFlow<PokeNotificationResponse?> get() = _pokeNotification

    fun getPokeNotification() {
        viewModelScope.launch {
            getPokeNotificationUseCase.invoke()
                .onSuccess {
                    _pokeNotification.value = it
                }
                .onFailure { Timber.e(it) }
        }
    }
}