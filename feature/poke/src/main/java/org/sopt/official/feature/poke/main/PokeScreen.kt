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
package org.sopt.official.feature.poke.main

import android.content.Intent
import android.view.View
import androidx.activity.compose.LocalActivity
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.context.findActivity
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeMainBinding
import org.sopt.official.feature.poke.friend.summary.FriendListSummaryActivity
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.feature.poke.util.isBestFriend
import org.sopt.official.feature.poke.util.isSoulMate
import org.sopt.official.feature.poke.util.setRelationStrokeColor
import org.sopt.official.feature.poke.util.showPokeToast
import org.sopt.official.model.UserStatus

@Composable
fun PokeScreen(
    paddingValues: PaddingValues,
    userStatus: UserStatus,
    navigateToPokeNotification: (String) -> Unit,
    viewModel: PokeMainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tracker = LocalTracker.current
    val activity = LocalActivity.current
    val coroutineScope = rememberCoroutineScope()

    var binding by remember {
        mutableStateOf<ActivityPokeMainBinding?>(null)
    }
    val fragmentActivity = remember {
        context.findActivity<FragmentActivity>()
    }

    val pokeMeUiState by viewModel.pokeMeUiState.collectAsStateWithLifecycle()
    val pokeFriendUiState by viewModel.pokeFriendUiState.collectAsStateWithLifecycle()
    val pokeSimilarFriendUiState by viewModel.pokeSimilarFriendUiState.collectAsStateWithLifecycle()
    val pokeUserUiState by viewModel.pokeUserUiState.collectAsStateWithLifecycle()
    val pokeAnonymousFriend by viewModel.anonymousFriend.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getPokeMe()
        viewModel.getPokeFriend()
        viewModel.getPokeSimilarFriends()
    }

    LifecycleResumeEffect(Unit) {
        tracker.track(
            type = EventType.VIEW,
            name = "poke_main",
            properties = mapOf("view_type" to userStatus.name)
        )

        onPauseOrDispose {}
    }

    val pokeMainListAdapter = remember {
        PokeMainListAdapter(object : PokeUserListClickListener {
            override fun onClickProfileImage(userId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties = mapOf(
                        "view_type" to userStatus.name,
                        "click_view_type" to "onboarding",
                        "view_profile" to userId
                    ),
                )
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, context.getString(R.string.poke_user_profile_url, userId).toUri())
                )
            }

            override fun onClickPokeButton(user: PokeUser) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_icon",
                    properties = mapOf(
                        "view_type" to userStatus.name,
                        "click_view_type" to "onboarding",
                        "view_profile" to user.userId,
                    ),
                )
                if (fragmentActivity != null) {
                    showMessageListBottomSheet(fragmentActivity, viewModel, user.userId, PokeMessageType.POKE_SOMEONE)
                }
            }
        })
    }

    LaunchedEffect(pokeSimilarFriendUiState) {
        binding?.let { currentBinding ->
            when (val state = pokeSimilarFriendUiState) {
                is UiState.Success -> {
                    pokeMainListAdapter.submitList(state.data)
                    currentBinding.refreshLayoutPokeMain.isRefreshing = false
                }
                is UiState.ApiError, is UiState.Failure -> {
                    activity?.showPokeToast(context.getString(R.string.toast_poke_error))
                    currentBinding.refreshLayoutPokeMain.isRefreshing = false
                }
                is UiState.Loading -> {}
            }
        }
    }

    LaunchedEffect(pokeMeUiState) {
        binding?.let { currentBinding ->
            when (val state = pokeMeUiState) {
                is UiState.Success -> {
                    if (activity is FragmentActivity) {
                        initPokeMeView(currentBinding, state.data, tracker, userStatus, activity, viewModel)
                    }
                }
                is UiState.ApiError, is UiState.Failure -> {
                    currentBinding.layoutSomeonePokeMe.setVisible(false)
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(pokeFriendUiState) {
        binding?.let { currentBinding ->
            when (val state = pokeFriendUiState) {
                is UiState.Success -> {
                    if (activity is FragmentActivity) {
                        initPokeFriendView(currentBinding, state.data, tracker, userStatus, activity, viewModel)
                    }
                }
                is UiState.ApiError -> activity?.showPokeToast(context.getString(R.string.toast_poke_error))
                is UiState.Failure -> activity?.showPokeToast(state.throwable.message ?: context.getString(R.string.toast_poke_error))
                else -> {}
            }
        }
    }

    LaunchedEffect(pokeUserUiState) {
        binding?.let { currentBinding ->
            when (val state = pokeUserUiState) {
                is UiState.Success -> {
                    viewModel.updatePokeUserState(state.data.userId)
                    val user = state.data

                    val isFriend = state.isFirstMeet && !user.isFirstMeet

                    if (isFriend) {
                        with(currentBinding) {
                            layoutLottie.visibility = View.VISIBLE
                            tvLottie.text = context.getString(
                                R.string.friend_complete,
                                if (user.isAnonymous) user.anonymousName else user.name
                            )
                            animationViewLottie.playAnimation()
                        }
                    } else {
                        if (isBestFriend(user.pokeNum, user.isAnonymous)) {
                            with(currentBinding) {
                                layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                tvFreindLottie.text = context.getString(R.string.anonymous_to_friend, user.anonymousName, "단짝친구가")
                                tvFreindLottieHint.text = context.getString(R.string.anonymous_user_info_part, user.generation, user.part)
                                animationFriendViewLottie.setAnimation(R.raw.friendtobestfriend)
                                animationFriendViewLottie.playAnimation()
                            }
                        } else if (isSoulMate(user.pokeNum, user.isAnonymous)) {
                            viewModel.setAnonymousFriend(user)
                            with(currentBinding) {
                                layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                tvFreindLottie.text = context.getString(R.string.anonymous_to_friend, user.anonymousName, "천생연분이")
                                animationFriendViewLottie.setAnimation(R.raw.bestfriendtosoulmate)
                                animationFriendViewLottie.playAnimation()
                            }
                        } else {
                            activity?.showPokeToast(context.getString(R.string.toast_poke_user_success))
                        }
                    }
                }
                is UiState.ApiError -> activity?.showPokeToast(context.getString(R.string.poke_user_alert_exceeded))
                is UiState.Failure -> activity?.showPokeToast(state.throwable.message ?: context.getString(R.string.toast_poke_error))
                else -> {}
            }
        }
    }

    AndroidViewBinding(
        factory = ActivityPokeMainBinding::inflate,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        binding = this
        if (recyclerViewPokeMain.adapter == null) {
            recyclerViewPokeMain.adapter = pokeMainListAdapter

            btnNextSomeonePokeMe.setOnClickListener {
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_alarm_detail",
                    properties = mapOf("view_type" to userStatus.name)
                )
                navigateToPokeNotification(userStatus.name)
            }

            imgNextPokeMyFriend.setOnClickListener {
                context.startActivity(
                    FriendListSummaryActivity.getIntent(
                        context,
                        FriendListSummaryActivity.StartArgs(userStatus.name),
                    ),
                )
            }

            scrollviewPokeMain.viewTreeObserver.addOnScrollChangedListener {
                refreshLayoutPokeMain.isEnabled = scrollviewPokeMain.scrollY == 0
            }

            refreshLayoutPokeMain.setOnRefreshListener {
                viewModel.getPokeMe()
                viewModel.getPokeFriend()
                viewModel.getPokeSimilarFriends()
            }

            animationViewLottie.addOnAnimationEndListener {
                layoutLottie.visibility = View.GONE
            }

            animationFriendViewLottie.addOnAnimationEndListener {
                if (pokeAnonymousFriend != null) {
                    coroutineScope.launch {
                        layoutAnonymousFriendLottie.visibility = View.GONE
                        layoutAnonymousFriendOpen.visibility = View.VISIBLE

                        pokeAnonymousFriend?.let {
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

            layoutLottie.setOnClickListener { }
        }
    }
}

private fun initPokeMeView(
    binding: ActivityPokeMainBinding,
    pokeMeItem: PokeUser,
    tracker: Tracker,
    userStatus: UserStatus,
    activity: FragmentActivity,
    viewModel: PokeMainViewModel
) {
    val context = binding.root.context
    with(binding) {
        layoutSomeonePokeMe.setVisible(true)
        imgUserProfileSomeonePokeMe.setOnClickListener {
            if (pokeMeItem.isAnonymous) return@setOnClickListener
            tracker.track(
                type = EventType.CLICK,
                name = "memberprofile",
                properties = mapOf(
                    "view_type" to userStatus.name,
                    "click_view_type" to "poke_main_alarm",
                    "view_profile" to pokeMeItem.userId
                ),
            )
            context.startActivity(Intent(Intent.ACTION_VIEW, context.getString(R.string.poke_user_profile_url, pokeMeItem.userId).toUri()))
        }

        if (pokeMeItem.isAnonymous) {
            pokeMeItem.anonymousImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfileSomeonePokeMe.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfileSomeonePokeMe.setImageResource(R.drawable.ic_empty_profile)
            tvUserNameSomeonePokeMe.text = pokeMeItem.anonymousName
            tvFriendsStatusSomeonePokeMe.visibility = View.GONE
            tvUserGenerationSomeonePokeMe.visibility = View.GONE
        } else {
            pokeMeItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfileSomeonePokeMe.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfileSomeonePokeMe.setImageResource(R.drawable.ic_empty_profile)
            tvUserNameSomeonePokeMe.text = pokeMeItem.name
            tvUserGenerationSomeonePokeMe.text = context.getString(R.string.poke_user_info, pokeMeItem.generation, pokeMeItem.part)
            tvFriendsStatusSomeonePokeMe.text =
                if (pokeMeItem.isFirstMeet) {
                    pokeMeItem.mutualRelationMessage
                } else {
                    "${pokeMeItem.relationName} ${pokeMeItem.pokeNum}콕"
                }
        }
        imgUserProfilePokeMeOutline.setRelationStrokeColor(pokeMeItem.relationName)
        tvUserMsgSomeonePokeMe.text = pokeMeItem.message
        btnSomeonePokeMe.isEnabled = !pokeMeItem.isAlreadyPoke
        btnSomeonePokeMe.setOnClickListener {
            tracker.track(
                type = EventType.CLICK,
                name = "poke_icon",
                properties =
                    mapOf(
                        "view_type" to userStatus.name,
                        "click_view_type" to "poke_main_alarm",
                        "view_profile" to pokeMeItem.userId,
                    ),
            )
            showMessageListBottomSheet(
                activity,
                viewModel,
                pokeMeItem.userId,
                when (pokeMeItem.isFirstMeet) {
                    true -> PokeMessageType.POKE_SOMEONE
                    false -> PokeMessageType.POKE_FRIEND
                },
                pokeMeItem.isFirstMeet,
            )
        }
    }
}

private fun initPokeFriendView(
    binding: ActivityPokeMainBinding,
    pokeFriendItem: PokeUser,
    tracker: Tracker,
    userStatus: UserStatus,
    activity: FragmentActivity,
    viewModel: PokeMainViewModel
) {
    val context = binding.root.context
    with(binding) {
        imgUserProfilePokeMyFriend.setOnClickListener {
            if (pokeFriendItem.isAnonymous) return@setOnClickListener

            tracker.track(
                type = EventType.CLICK,
                name = "memberprofile",
                properties =
                    mapOf(
                        "view_type" to userStatus.name,
                        "click_view_type" to "poke_main_friend",
                        "view_profile" to pokeFriendItem.userId,
                    ),
            )
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    context.getString(R.string.poke_user_profile_url, pokeFriendItem.userId).toUri()
                )
            )
        }

        if (pokeFriendItem.isAnonymous) {
            imgUserProfilePokeMyFriend.load(pokeFriendItem.anonymousImage) { transformations(CircleCropTransformation()) }
            tvUserNamePokeMyFriend.text = pokeFriendItem.anonymousName
        } else {
            pokeFriendItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfilePokeMyFriend.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfilePokeMyFriend.setImageResource(R.drawable.ic_empty_profile)
            tvUserNamePokeMyFriend.text = pokeFriendItem.name
            tvUserGenerationSomeonePokeMe.text = context.getString(R.string.poke_user_info, pokeFriendItem.generation, pokeFriendItem.part)
        }
        imgUserProfilePokeMyFriendOutline.setRelationStrokeColor(pokeFriendItem.relationName)
        tvCountPokeMyFriend.text = "${pokeFriendItem.pokeNum}콕"
        btnPokeMyFriend.isEnabled = !pokeFriendItem.isAlreadyPoke
        btnPokeMyFriend.setOnClickListener {
            tracker.track(
                type = EventType.CLICK,
                name = "poke_icon",
                properties =
                    mapOf(
                        "view_type" to userStatus.name,
                        "click_view_type" to "poke_main_friend",
                        "view_profile" to pokeFriendItem.userId,
                    ),
            )
            showMessageListBottomSheet(activity, viewModel, pokeFriendItem.userId, PokeMessageType.POKE_FRIEND)
        }
    }
}

private fun showMessageListBottomSheet(
    activity: FragmentActivity,
    viewModel: PokeMainViewModel,
    userId: Int,
    pokeMessageType: PokeMessageType,
    isFirstMeet: Boolean = false
) {
    val messageListBottomSheet =
        MessageListBottomSheetFragment.Builder()
            .setMessageListType(pokeMessageType)
            .onClickMessageListItem { message, isAnonymous ->
                viewModel.pokeUser(
                    userId = userId,
                    isAnonymous = isAnonymous,
                    message = message,
                    isFirstMeet = isFirstMeet
                )
            }
            .create()

    messageListBottomSheet.show(activity.supportFragmentManager, messageListBottomSheet.tag)
}
