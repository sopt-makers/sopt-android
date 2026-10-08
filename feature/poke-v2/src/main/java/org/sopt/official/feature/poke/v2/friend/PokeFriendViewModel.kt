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
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.domain.poke.usecase.GetFriendListDetailUseCase
import org.sopt.official.domain.poke.usecase.GetFriendListSummaryUseCase
import org.sopt.official.domain.poke.usecase.GetPokeMessageListUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.feature.poke.v2.component.PokeSnackBarType
import org.sopt.official.feature.poke.v2.friend.model.FriendListSheetState
import org.sopt.official.feature.poke.v2.friend.model.MessageSheetState
import org.sopt.official.feature.poke.v2.friend.model.PokeRelationChangeState
import org.sopt.official.feature.poke.v2.friend.model.toPokeFriendListSections
import org.sopt.official.feature.poke.v2.friend.navigation.PokeFriend
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeUserUiState
import javax.inject.Inject

@HiltViewModel
class PokeFriendViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFriendListDetailUseCase: GetFriendListDetailUseCase,
    private val getFriendListSummaryUseCase: GetFriendListSummaryUseCase,
    private val getPokeMessageListUseCase: GetPokeMessageListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    private val friendType: PokeFriendType? = savedStateHandle.toRoute<PokeFriend>().friendType

    private val _uiState = MutableStateFlow(PokeFriendState())
    val uiState: StateFlow<PokeFriendState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PokeFriendSideEffect>()
    val sideEffect: SharedFlow<PokeFriendSideEffect> = _sideEffect.asSharedFlow()

    private var totalPageSize = -1
    private var currentPaginationIndex = 0
    private var friendListJob: Job? = null

    private var messageListJob: Job? = null
    private var pokeJob: Job? = null

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

    fun openMessageSheet(target: PokeUserUiState) {
        messageListJob?.cancel()
        _uiState.update { it.copy(messageSheet = MessageSheetState(target = target)) }

        messageListJob = viewModelScope.launch {
            getPokeMessageListUseCase(messageType = PokeMessageType.POKE_FRIEND)
                .onSuccess { messageList ->
                    _uiState.update { state ->
                        state.copy(
                            messageSheet = state.messageSheet?.copy(
                                title = messageList.header,
                                messages = messageList.messages
                                    .map { it.toPokeMessageUiState() }
                                    .toImmutableList(),
                            ),
                        )
                    }
                }
                .onApiError { _, _ ->
                    emitErrorSnackbar(isMessageSheet = true)
                }
                .onFailure { throwable ->
                    emitErrorSnackbar(throwable, isMessageSheet = true)
                }
        }
    }

    fun toggleMessageAnonymous() {
        val sheet = _uiState.value.messageSheet ?: return

        if (sheet.target.isAnonymousCheckboxLocked) {
            viewModelScope.launch {
                _sideEffect.emit(
                    PokeFriendSideEffect.ShowSnackbar(
                        message = "천생연분은 실명으로만 콕찌를 수 있어요.",
                        isMessageSheet = true,
                    ),
                )
            }
            return
        }

        val isAnonymous = !sheet.isAnonymous
        _uiState.update { state ->
            state.copy(messageSheet = state.messageSheet?.copy(isAnonymous = isAnonymous))
        }

        if (!isAnonymous) {
            viewModelScope.launch {
                _sideEffect.emit(
                    PokeFriendSideEffect.ShowSnackbar(
                        message = "익명 해제 시, 상대방이 나를 알 수 있어요.",
                        isMessageSheet = true,
                    ),
                )
            }
        }
    }

    fun closeMessageSheet() {
        messageListJob?.cancel()
        _uiState.update { it.copy(messageSheet = null) }
    }

    fun pokeUser(message: PokeMessageUiState) {
        val sheet = _uiState.value.messageSheet ?: return
        if (pokeJob?.isActive == true) return

        pokeJob = viewModelScope.launch {
            pokeUserUseCase(
                userId = sheet.target.userId,
                isAnonymous = sheet.isAnonymous && !sheet.target.isAnonymousCheckboxLocked,
                message = message.content,
            )
                .onSuccess { pokedUser ->
                    closeMessageSheet()
                    markPokedFriend(userId = sheet.target.userId)
                    getFriendListSummary()
                    showPokeResult(pokedUser)
                }
                .onApiError { _, _ ->
                    closeMessageSheet()
                    emitErrorSnackbar()
                }
                .onFailure { throwable ->
                    closeMessageSheet()
                    emitErrorSnackbar(throwable)
                }
        }
    }

    private fun markPokedFriend(userId: Int) {
        _uiState.update { state ->
            state.copy(
                friendListSheet = state.friendListSheet?.let { sheet ->
                    sheet.copy(
                        friends = sheet.friends.map { friend ->
                            if (friend.userId == userId) {
                                friend.copy(pokeCount = friend.pokeCount + 1, isPokeButtonEnabled = false)
                            } else {
                                friend
                            }
                        }.toImmutableList(),
                    )
                },
            )
        }
    }

    private suspend fun showPokeResult(pokedUser: PokeUser) {
        val user = pokedUser.toPokeUserUiState()
        val relationChange = when {
            user.isBestFriend -> PokeRelationChangeState.BestFriend(user)
            user.isSoulMate -> PokeRelationChangeState.Soulmate(user)
            else -> null
        }

        if (relationChange == null) {
            _sideEffect.emit(
                PokeFriendSideEffect.ShowSnackbar(
                    message = "콕 찌르기를 완료했어요.",
                    type = PokeSnackBarType.SUCCESS,
                ),
            )
            return
        }

        _uiState.update { state ->
            if (state.relationChange.isLottiePlaying()) state else state.copy(relationChange = relationChange)
        }
    }

    private fun PokeRelationChangeState?.isLottiePlaying(): Boolean =
        this is PokeRelationChangeState.BestFriend || this is PokeRelationChangeState.Soulmate

    fun finishRelationChangeLottie() {
        _uiState.update { state ->
            val next = (state.relationChange as? PokeRelationChangeState.Soulmate)
                ?.let { PokeRelationChangeState.SoulmateRevealed(it.user) }
            state.copy(relationChange = next)
        }
    }

    fun finishSoulmateReveal() {
        _uiState.update { it.copy(relationChange = null) }
    }

    private suspend fun emitErrorSnackbar(
        throwable: Throwable? = null,
        isMessageSheet: Boolean = false,
    ) {
        _sideEffect.emit(
            PokeFriendSideEffect.ShowSnackbar(
                message = throwable?.message ?: "문제가 발생했습니다.",
                type = PokeSnackBarType.FAILURE,
                isMessageSheet = isMessageSheet,
            ),
        )
    }
}
