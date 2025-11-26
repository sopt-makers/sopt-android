package org.sopt.official.feature.poke.friend.summary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.context.findActivity
import org.sopt.official.common.util.colorOf
import org.sopt.official.common.util.dp
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityFriendListSummaryBinding
import org.sopt.official.feature.poke.databinding.IncludeFriendListBlockSmallBinding
import org.sopt.official.feature.poke.friend.detail.FriendListDetailBottomSheetFragment
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.ItemDecorationDivider
import org.sopt.official.feature.poke.user.PokeUserListAdapter
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.user.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.feature.poke.util.dismissBottomSheet
import org.sopt.official.feature.poke.util.isBestFriend
import org.sopt.official.feature.poke.util.isSoulMate
import org.sopt.official.feature.poke.util.setRelationStrokeColor
import org.sopt.official.feature.poke.util.showPokeToast
import org.sopt.official.model.UserStatus

@Composable
fun FriendListSummaryScreen(
    paddingValues: PaddingValues,
    userStatus: UserStatus,
    viewModel: FriendListSummaryViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tracker = LocalTracker.current
    val coroutineScope = rememberCoroutineScope()

    val initialFriendType = viewModel.friendType.initialFriendType

    val binding = remember {
        ActivityFriendListSummaryBinding.inflate(LayoutInflater.from(context))
    }

    val fragmentActivity = remember {
        context.findActivity<FragmentActivity>()
    }

    val fragmentManager = fragmentActivity?.supportFragmentManager
    val bottomSheetTag = "MessageListBottomSheet"

    val friendListSummaryUiState by viewModel.friendListSummaryUiState.collectAsStateWithLifecycle()
    val pokeUserUiState by viewModel.pokeUserUiState.collectAsStateWithLifecycle()
    val anonymousFriend by viewModel.anonymousFriend.collectAsStateWithLifecycle()

    val recyclerViewItemDecorationDivider = remember {
        ItemDecorationDivider(
            color = context.colorOf(org.sopt.official.designsystem.R.color.mds_gray_800),
            height = 1.dp,
        )
    }

    val pokeUserListClickLister = remember {
        object : PokeUserListClickListener {
            override fun onClickProfileImage(userId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties =
                        mapOf(
                            "view_type" to userStatus.name,
                            "click_view_type" to "friend",
                            "view_profile" to userId,
                        ),
                )
                val intent = Intent(Intent.ACTION_VIEW, context.getString(R.string.poke_user_profile_url, userId).toUri())
                context.startActivity(intent)
            }

            override fun onClickPokeButton(user: PokeUser) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_icon",
                    properties =
                        mapOf(
                            "view_type" to userStatus.name,
                            "click_view_type" to "friend",
                            "view_profile" to user.userId,
                        ),
                )

                if (fragmentManager != null) {
                    val bottomSheet = MessageListBottomSheetFragment.Builder()
                        .setMessageListType(PokeMessageType.POKE_FRIEND)
                        .onClickMessageListItem { message, isAnonymous ->
                            viewModel.pokeUser(
                                userId = user.userId,
                                isAnonymous = isAnonymous,
                                message = message
                            )
                        }
                        .create()

                    bottomSheet.show(fragmentManager, bottomSheetTag)
                }
            }
        }
    }

    val newFriendListAdapter = remember {
        PokeUserListAdapter(PokeUserListItemViewType.SMALL, pokeUserListClickLister)
    }
    val bestFriendListAdapter = remember {
        PokeUserListAdapter(PokeUserListItemViewType.SMALL, pokeUserListClickLister)
    }
    val soulmateListAdapter = remember {
        PokeUserListAdapter(PokeUserListItemViewType.SMALL, pokeUserListClickLister)
    }

    val detailSheetLauncher: (PokeFriendType) -> Unit = { type ->
        showFriendListDetailBottomSheet(
            fragmentManager = fragmentManager,
            userStatus = userStatus,
            pokeFriendType = type
        )
    }

    LaunchedEffect(friendListSummaryUiState) {
        when (val state = friendListSummaryUiState) {
            is UiState.Loading -> {}
            is UiState.Success<FriendListSummary> -> {
                fragmentActivity?.let {
                    updateRecyclerView(binding, state.data, it, newFriendListAdapter, bestFriendListAdapter, soulmateListAdapter)
                }
            }
            is UiState.ApiError -> fragmentActivity?.showPokeToast(context.getString(R.string.toast_poke_error))
            is UiState.Failure -> fragmentActivity?.showPokeToast(state.throwable.message ?: context.getString(R.string.toast_poke_error))
        }
    }

    LaunchedEffect(pokeUserUiState) {
        when (val state = pokeUserUiState) {
            is UiState.Loading -> {}
            is UiState.Success<PokeUser> -> {
                dismissBottomSheet(
                    fragmentManager = fragmentManager,
                    bottomSheetTag = bottomSheetTag
                )
                viewModel.getFriendListSummary()

                val user = state.data

                val isBestFriend = isBestFriend(user.pokeNum, user.isAnonymous)
                val isSoulMate = isSoulMate(user.pokeNum, user.isAnonymous)

                if (isBestFriend) {
                    with(binding) {
                        layoutAnonymousFriendLottie.visibility = View.VISIBLE
                        tvFreindLottie.text = context.getString(R.string.anonymous_to_friend, user.anonymousName, "단짝친구가")
                        tvFreindLottieHint.text =
                            context.getString(R.string.anonymous_user_info_part, user.generation, user.part)
                        animationFriendViewLottie.setAnimation(R.raw.friendtobestfriend)
                        animationFriendViewLottie.playAnimation()
                    }
                } else if (isSoulMate) {
                    viewModel.setAnonymousFriend(user)
                    with(binding) {
                        layoutAnonymousFriendLottie.visibility = View.VISIBLE
                        tvFreindLottie.text = context.getString(R.string.anonymous_to_friend, user.anonymousName, "천생연분이")
                        animationFriendViewLottie.setAnimation(R.raw.bestfriendtosoulmate)
                        animationFriendViewLottie.playAnimation()
                    }
                } else {
                    fragmentActivity?.showPokeToast(context.getString(R.string.toast_poke_user_success))
                }
            }

            is UiState.ApiError -> {
                dismissBottomSheet(fragmentManager, bottomSheetTag)
                fragmentActivity?.showPokeToast(context.getString(R.string.poke_user_alert_exceeded))
            }

            is UiState.Failure -> {
                dismissBottomSheet(fragmentManager, bottomSheetTag)
                fragmentActivity?.showPokeToast(state.throwable.message ?: context.getString(R.string.toast_poke_error))
            }
        }
    }

    LaunchedEffect(initialFriendType) {
        if (!initialFriendType.isNullOrEmpty()) {
            val initialType = PokeFriendType.entries.find {
                it.typeName.equals(initialFriendType, ignoreCase = true)
            }
            initialType?.let {
                showFriendListDetailBottomSheet(
                    fragmentManager = fragmentManager,
                    pokeFriendType = it,
                    userStatus = userStatus
                )
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        tracker.track(
            EventType.VIEW,
            name = "view_poke_alarm_detail",
            properties = mapOf("view_type" to userStatus.name),
        )

        with(binding) {
            layoutAnonymousFriendOpen.visibility = View.GONE
            layoutAnonymousFriendLottie.visibility = View.GONE
        }

        onPauseOrDispose {  }
    }



    AndroidView(
        factory = { _ ->
            with(binding) {
                includeAppBar.textViewTitle.text = binding.root.context.getString(R.string.poke_title)

                swipeRefreshLayout.setOnRefreshListener {
                    viewModel.getFriendListSummary()
                    swipeRefreshLayout.isRefreshing = false
                }

                animationFriendViewLottie.addOnAnimationEndListener {
                    if (anonymousFriend != null) { // 천생연분 -> 정체 공개
                        coroutineScope.launch {
                            // 로티
                            layoutAnonymousFriendLottie.visibility = View.GONE
                            layoutAnonymousFriendOpen.visibility = View.VISIBLE

                            anonymousFriend?.let {
                                tvAnonymousFreindName.text = context.getString(R.string.anonymous_user_identity, it.anonymousName)
                                tvAnonymousFreindInfo.text = context.getString(R.string.anonymous_user_info, it.generation, it.part, it.name)
                                imgAnonymousFriendOpen.load(it.profileImage.ifEmpty { R.drawable.ic_empty_profile }) {
                                    transformations(CircleCropTransformation())
                                }

                                imgAnonymousFriendOpenOutline.setRelationStrokeColor(it.mutualRelationMessage)
                            }

                            delay(2000)
                            layoutAnonymousFriendOpen.visibility = View.GONE
                            viewModel.setAnonymousFriend(null)
                        }
                    } else {
                        layoutAnonymousFriendLottie.visibility = View.GONE
                    }
                }

                initFriendListBlock(
                    includeFriendListBlockNewFriend, PokeFriendType.NEW, newFriendListAdapter,
                    context, recyclerViewItemDecorationDivider, detailSheetLauncher
                )
                initFriendListBlock(
                    includeFriendListBlockBestFriend, PokeFriendType.BEST_FRIEND, bestFriendListAdapter,
                    context, recyclerViewItemDecorationDivider, detailSheetLauncher
                )
                initFriendListBlock(
                    includeFriendListBlockSoulmate, PokeFriendType.SOULMATE, soulmateListAdapter,
                    context, recyclerViewItemDecorationDivider, detailSheetLauncher
                )
            }
            binding.root
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    )
}

fun showFriendListDetailBottomSheet(
    fragmentManager: FragmentManager?,
    userStatus: UserStatus,
    pokeFriendType: PokeFriendType
) {
    if (fragmentManager != null) {
        FriendListDetailBottomSheetFragment.Builder()
            .setUserStatus(userStatus.name)
            .setPokeFriendType(pokeFriendType)
            .create()
            .let { it.show(fragmentManager, it.tag) }
    }
}

private fun updateRecyclerView(
    binding: ActivityFriendListSummaryBinding,
    data: FriendListSummary,
    fragmentActivity: FragmentActivity,
    newFriendListAdapter: PokeUserListAdapter,
    bestFriendListAdapter: PokeUserListAdapter,
    soulmateListAdapter: PokeUserListAdapter
) {
    binding.includeFriendListBlockNewFriend.apply {
        val list = data.newFriend
        val size = data.newFriendSize
        recyclerView.setVisible(list.isNotEmpty())
        includeFriendListEmptyView.root.setVisible(list.isEmpty())
        textViewListCount.text = fragmentActivity.getString(R.string.friend_list_count, size)
        newFriendListAdapter.submitList(list)
    }

    binding.includeFriendListBlockBestFriend.apply {
        val list = data.bestFriend
        val size = data.bestFriendSize
        recyclerView.setVisible(list.isNotEmpty())
        includeFriendListEmptyView.root.setVisible(list.isEmpty())
        textViewListCount.text = fragmentActivity.getString(R.string.friend_list_count, size)
        bestFriendListAdapter.submitList(list)
    }

    binding.includeFriendListBlockSoulmate.apply {
        val list = data.soulmate
        val size = data.soulmateSize
        recyclerView.setVisible(list.isNotEmpty())
        includeFriendListEmptyView.root.setVisible(list.isEmpty())
        textViewListCount.text = fragmentActivity.getString(R.string.friend_list_count, size)
        soulmateListAdapter.submitList(list)
    }
}

private fun initFriendListBlock(
    binding: IncludeFriendListBlockSmallBinding,
    type: PokeFriendType,
    adapter: PokeUserListAdapter,
    context: Context,
    itemDecoration: ItemDecorationDivider,
    showDetailSheet: (PokeFriendType) -> Unit
) {
    binding.apply {
        imageButton.setBackgroundResource(R.drawable.icon_chevron)
        imageButton.setOnClickListener { showDetailSheet(type) }
        textViewFriendListTitle.text = when (type) {
            PokeFriendType.NEW -> context.getString(R.string.friend_relation_name_new_friend)
            PokeFriendType.BEST_FRIEND -> context.getString(R.string.friend_relation_name_best_friend)
            PokeFriendType.SOULMATE -> context.getString(R.string.friend_relation_name_soulmate)
        }
        textViewFriendListDescription.text = when (type) {
            PokeFriendType.NEW -> context.getString(R.string.friend_relation_description_new_friend)
            PokeFriendType.BEST_FRIEND -> context.getString(R.string.friend_relation_description_best_friend)
            PokeFriendType.SOULMATE -> context.getString(R.string.friend_relation_description_soulmate)
        }
        textViewListCount.text = context.getString(R.string.friend_list_count, 0)

        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = adapter
    }
}