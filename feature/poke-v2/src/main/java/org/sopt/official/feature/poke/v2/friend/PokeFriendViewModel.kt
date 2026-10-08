package org.sopt.official.feature.poke.v2.friend

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.usecase.GetFriendListDetailUseCase
import org.sopt.official.domain.poke.usecase.GetFriendListSummaryUseCase
import org.sopt.official.feature.poke.v2.component.PokeSnackBarType
import org.sopt.official.feature.poke.v2.friend.model.FriendListSheetState
import org.sopt.official.feature.poke.v2.friend.model.toPokeFriendListSections
import org.sopt.official.feature.poke.v2.friend.navigation.PokeFriend
import org.sopt.official.feature.poke.v2.main.model.toPokeUserUiState
import javax.inject.Inject

@HiltViewModel
class PokeFriendViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFriendListDetailUseCase: GetFriendListDetailUseCase,
    private val getFriendListSummaryUseCase: GetFriendListSummaryUseCase,
) : ViewModel() {
    private val friendType: PokeFriendType? = savedStateHandle.toRoute<PokeFriend>().friendType

    private val _uiState = MutableStateFlow(PokeFriendState())
    val uiState: StateFlow<PokeFriendState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PokeFriendSideEffect>()
    val sideEffect: SharedFlow<PokeFriendSideEffect> = _sideEffect.asSharedFlow()

    private var totalPageSize = -1
    private var currentPaginationIndex = 0
    private var friendListJob: Job? = null
    init {
        getFriendListSummary()
        friendType?.let(::openFriendListSheet)
    }
    fun getFriendListSummary() {
        viewModelScope.launch {
            getFriendListSummaryUseCase()
                .onSuccess { summary ->
                    _uiState.update { it.copy(sections = summary.toPokeFriendListSections()) }
                }
                .onApiError { _, _ ->
                    emitErrorSnackbar()
                }
                .onFailure { throwable ->
                    emitErrorSnackbar(throwable)
                }
        }
    }

    fun openFriendListSheet(type: PokeFriendType) {
        friendListJob?.cancel()
        totalPageSize = -1
        currentPaginationIndex = 0

        _uiState.update { it.copy(friendListSheet = FriendListSheetState(type = type)) }
        loadMoreFriendList()
    }

    fun loadMoreFriendList() {
        val sheet = _uiState.value.friendListSheet ?: return
        if (friendListJob?.isActive == true) return
        if (currentPaginationIndex == totalPageSize) return

        friendListJob = viewModelScope.launch {
            getFriendListDetailUseCase(
                type = sheet.type,
                page = currentPaginationIndex,
            )
                .onSuccess { response ->
                    totalPageSize = response.totalPageSize
                    currentPaginationIndex = response.pageNum
                    _uiState.update { state ->
                        state.copy(
                            friendListSheet = state.friendListSheet?.let { sheet ->
                                val friends = (sheet.friends + response.friendList.map { it.toPokeUserUiState() }).toImmutableList()
                                sheet.copy(friendCount = friends.size, friends = friends)
                            },
                        )
                    }
                }
                .onApiError { _, _ ->
                    emitErrorSnackbar()
                }
                .onFailure { throwable ->
                    emitErrorSnackbar(throwable)
                }
        }
    }

    fun closeFriendListSheet() {
        friendListJob?.cancel()
        _uiState.update { it.copy(friendListSheet = null, relationChange = null) }
    }
    private suspend fun emitErrorSnackbar(throwable: Throwable? = null) {
        _sideEffect.emit(
            PokeFriendSideEffect.ShowSnackbar(
                message = throwable?.message ?: "문제가 발생했습니다.",
                type = PokeSnackBarType.FAILURE,
            ),
        )
    }
}
