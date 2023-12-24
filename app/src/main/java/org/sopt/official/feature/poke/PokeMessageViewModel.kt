package org.sopt.official.feature.poke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.data.model.poke.response.PokeMessageResponse
import org.sopt.official.domain.usecase.poke.GetPokeMessageUseCase
import org.sopt.official.feature.poke.enums.MessageType
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokeMessageViewModel @Inject constructor(
    private val getPokeMessageUseCase: GetPokeMessageUseCase
) : ViewModel() {
    private val _pokeMessage = MutableStateFlow<PokeMessageResponse?>(null)
    val pokeMessage: StateFlow<PokeMessageResponse?> get() = _pokeMessage

    fun getPokeMessage(messageType: MessageType) {
        viewModelScope.launch {
            getPokeMessageUseCase.invoke(messageType)
                .onSuccess {
                    _pokeMessage.value = it
                }
                .onFailure { Timber.e(it) }
        }
    }
}