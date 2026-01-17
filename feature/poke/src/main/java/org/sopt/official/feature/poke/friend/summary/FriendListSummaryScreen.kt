/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.poke.friend.summary

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dev.zacsweers.metro.viewmodel.compose.metroViewModel
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
    viewModel: FriendListSummaryViewModel = metroViewModel()
) {
    val context = LocalContext.current
    val tracker = LocalTracker.current
    val coroutineScope = rememberCoroutineScope()

    val initialFriendType = viewModel.friendType.initialFriendType

    var binding by remember {
        mutableStateOf<ActivityFriendListSummaryBinding?>(null)
    }

    val fragmentActivity = remember {
        context.findActivity<FragmentActivity>()
    }

    val fragmentManager = fragmentActivity?.supportFragmentManager
    val bottomSheetTag = "MessageListBottomSheet"

    val friendListSummaryUiState by viewModel.friendListSummaryUiState.collectAsStateWithLifecycle()
    val pokeUserUiState by viewModel.pokeUserUiState.collectAsStateWithLifecycle()
    val anonymousFriend by viewModel.anonymousFriend.collectAsStateWithLifecycle()

    val recyclerViewItemDecorationDivider = ItemDecorationDivider(
        color = context.colorOf(org.sopt.official.designsystem.R.color.mds_gray_800),
        height = 1.dp,
    )

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
                    val bottomSheet = fragmentManager.fragmentFactory.instantiate(
                        MessageListBottomSheetFragment::class.java.classLoader!!,
                        MessageListBottomSheetFragment::class.java.name
                    ) as MessageListBottomSheetFragment

                    bottomSheet.arguments = android.os.Bundle().apply {
                        putSerializable("pokeMessageType", PokeMessageType.POKE_FRIEND)
                    }

                    bottomSheet.onClickMessageListItem = { message, isAnonymous ->
                        viewModel.pokeUser(
                            userId = user.userId,
                            isAnonymous = isAnonymous,
                            message = message
                        )
                    }

                    bottomSheet.show(fragmentManager, bottomSheetTag)
                }
            }
        }
    }

    val newFriendListAdapter = remember { PokeUserListAdapter(PokeUserListItemViewType.SMALL, pokeUserListClickLister) }
    val bestFriendListAdapter = remember { PokeUserListAdapter(PokeUserListItemViewType.SMALL, pokeUserListClickLister) }
    val soulmateListAdapter = remember { PokeUserListAdapter(PokeUserListItemViewType.SMALL, pokeUserListClickLister) }

    val detailSheetLauncher: (PokeFriendType) -> Unit = { type ->
        showFriendListDetailBottomSheet(
            fragmentManager = fragmentManager,
            userStatus = userStatus,
            pokeFriendType = type
        )
    }

    LaunchedEffect(friendListSummaryUiState) {
        binding?.let { currentBinding ->
            when (val state = friendListSummaryUiState) {
                is UiState.Success<FriendListSummary> -> {
                    fragmentActivity?.let { activity ->
                        updateRecyclerView(currentBinding, state.data, activity, newFriendListAdapter, bestFriendListAdapter, soulmateListAdapter)
                    }
                    currentBinding.swipeRefreshLayout.isRefreshing = false
                }
                is UiState.ApiError -> {
                    fragmentActivity?.showPokeToast(context.getString(R.string.toast_poke_error))
                    currentBinding.swipeRefreshLayout.isRefreshing = false
                }
                is UiState.Failure -> {
                    fragmentActivity?.showPokeToast(state.throwable.message ?: context.getString(R.string.toast_poke_error))
                    currentBinding.swipeRefreshLayout.isRefreshing = false
                }
                is UiState.Loading -> {}
            }
        }
    }

    LaunchedEffect(pokeUserUiState) {
        binding?.let { currentBinding ->
            when (val state = pokeUserUiState) {
                is UiState.Success<PokeUser> -> {
                    dismissBottomSheet(fragmentManager = fragmentManager, bottomSheetTag = bottomSheetTag)
                    viewModel.getFriendListSummary()

                    val user = state.data
                    val isBestFriend = isBestFriend(user.pokeNum, user.isAnonymous)
                    val isSoulMate = isSoulMate(user.pokeNum, user.isAnonymous)

                    if (!isBestFriend && !isSoulMate) {
                        fragmentActivity?.showPokeToast(context.getString(R.string.toast_poke_user_success))
                    } else {
                        if (!currentBinding.animationFriendViewLottie.isAnimating) {
                            if (isBestFriend) {
                                currentBinding.layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                currentBinding.tvFreindLottie.text = context.getString(R.string.anonymous_to_friend, user.anonymousName, "단짝친구가")
                                currentBinding.tvFreindLottieHint.text = context.getString(R.string.anonymous_user_info_part, user.generation, user.part)
                                currentBinding.animationFriendViewLottie.setAnimation(R.raw.friendtobestfriend)
                                currentBinding.animationFriendViewLottie.playAnimation()
                            } else if (isSoulMate) {
                                currentBinding.layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                currentBinding.tvFreindLottie.text = context.getString(R.string.anonymous_to_friend, user.anonymousName, "천생연분이")
                                currentBinding.animationFriendViewLottie.setAnimation(R.raw.bestfriendtosoulmate)
                                currentBinding.animationFriendViewLottie.playAnimation()
                            }
                        }
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
                else -> {}
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

        onPauseOrDispose {  }
    }

    AndroidViewBinding(
        factory = ActivityFriendListSummaryBinding::inflate,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        binding = this
        if (includeFriendListBlockNewFriend.recyclerView.adapter == null) {
            includeAppBar.textViewTitle.text = root.context.getString(R.string.poke_title)

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getFriendListSummary()
                swipeRefreshLayout.isRefreshing = false
            }

            initFriendListBlock(
                includeFriendListBlockNewFriend, PokeFriendType.NEW, newFriendListAdapter,
                context, recyclerViewItemDecorationDivider, detailSheetLauncher
            )
            initFriendListBlock(
                includeFriendListBlockBestFriend, PokeFriendType.BEST_FRIEND,
                bestFriendListAdapter, context, recyclerViewItemDecorationDivider, detailSheetLauncher
            )
            initFriendListBlock(
                includeFriendListBlockSoulmate, PokeFriendType.SOULMATE,
                soulmateListAdapter, context, recyclerViewItemDecorationDivider, detailSheetLauncher
            )

            animationFriendViewLottie.addOnAnimationEndListener {
                if (anonymousFriend != null) {
                    coroutineScope.launch {
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
        }
    }
}

fun showFriendListDetailBottomSheet(
    fragmentManager: FragmentManager?,
    userStatus: UserStatus,
    pokeFriendType: PokeFriendType
) {
    if (fragmentManager != null) {
        val fragment = fragmentManager.fragmentFactory.instantiate(
            FriendListDetailBottomSheetFragment::class.java.classLoader!!,
            FriendListDetailBottomSheetFragment::class.java.name
        ) as FriendListDetailBottomSheetFragment

        fragment.arguments = android.os.Bundle().apply {
            putString("userStatus", userStatus.name)
            putSerializable("pokeFriendType", pokeFriendType)
        }

        fragment.show(fragmentManager, fragment.tag)
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
