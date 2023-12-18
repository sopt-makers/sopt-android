package org.sopt.official.feature.poke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse
import org.sopt.official.domain.usecase.poke.GetPokeFriendUseCase
import org.sopt.official.domain.usecase.poke.GetPokeMeUseCase
import timber.log.Timber
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class PokeMainViewModel @Inject constructor(
    private val getPokeMeUseCase: GetPokeMeUseCase,
    private val getPokeFriendUseCase: GetPokeFriendUseCase
) : ViewModel() {
    private val _pokeMe = MutableStateFlow<PokeMeResponse?>(null)
    val pokeMe: StateFlow<PokeMeResponse?> get() = _pokeMe

    fun getPokeMe() {
        viewModelScope.launch {
            getPokeMeUseCase.invoke()
                .onSuccess {
                    _pokeMe.value = it
                }
                .onFailure { Timber.e(it) }
        }
    }

    private val _pokeFriend = MutableStateFlow<PokeFriendResponse?>(null)
    val pokeFriend: StateFlow<PokeFriendResponse?> get() = _pokeFriend

    fun getPokeFriend() {
        viewModelScope.launch {
            getPokeFriendUseCase.invoke()
                .onSuccess {
                    _pokeFriend.value = it
                }
                .onFailure { Timber.e(it) }
        }
    }
}