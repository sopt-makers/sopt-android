package org.sopt.official.feature.poke.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.PokeNotificationItem
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.use_case.GetPokeNotificationListUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokeNotificationViewModel @Inject constructor(
    private val getPokeNotificationListUseCase: GetPokeNotificationListUseCase
) : ViewModel() {

    private val _pokeNotification = MutableStateFlow<List<PokeNotificationItem>>(arrayListOf())
    val pokeNotification: StateFlow<List<PokeNotificationItem>> get() = _pokeNotification

    private var currentPaginationIndex = 0
    private var pokeNotificationJob: Job? = null

    init {
        getPokeNotification()
    }

    fun getPokeNotification() {
        pokeNotificationJob?.let {
            if (it.isActive || !it.isCompleted) return
        }
        viewModelScope.launch {
            getPokeNotificationListUseCase.invoke(currentPaginationIndex)
                .onSuccess {
//                    _pokeNotification.value = _pokeNotification.value.plus(it)
                    currentPaginationIndex++
                }
                .onFailure { Timber.e(it) }
        }
    }
}