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
package org.sopt.official.feature.poke.notification

import android.content.Intent
import android.view.View
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.context.findActivity
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeNotificationBinding
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.feature.poke.util.dismissBottomSheet
import org.sopt.official.feature.poke.util.isBestFriend
import org.sopt.official.feature.poke.util.isSoulMate
import org.sopt.official.feature.poke.util.setRelationStrokeColor
import org.sopt.official.feature.poke.util.showPokeToast
import org.sopt.official.model.UserStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeNotificationScreen(
    paddingValues: PaddingValues,
    userStatus: UserStatus,
    viewModel: PokeNotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tracker = LocalTracker.current
    val coroutineScope = rememberCoroutineScope()

    val pokeNotificationUiState by viewModel.pokeNotification.collectAsStateWithLifecycle()
    val pokeUserUiState by viewModel.pokeUserUiState.collectAsStateWithLifecycle()
    val anonymousFriend by viewModel.anonymousFriend.collectAsStateWithLifecycle()

    var binding by remember {
        mutableStateOf<ActivityPokeNotificationBinding?>(null)
    }

    val fragmentActivity = remember(context) {
        context.findActivity<FragmentActivity>()
    }

    val fragmentManager = fragmentActivity?.supportFragmentManager
    val bottomSheetTag = "MessageListBottomSheet"

    val adapter = remember {
        PokeNotificationAdapter(object : PokeUserListClickListener {
            override fun onClickProfileImage(userId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties = mapOf(
                        "view_type" to userStatus.name,
                        "click_view_type" to "poke_alarm",
                        "view_profile" to userId,
                    ),
                )
                val intent = Intent(Intent.ACTION_VIEW, context.getString(R.string.poke_user_profile_url, userId).toUri())
                context.startActivity(intent)
            }

            override fun onClickPokeButton(user: PokeUser) {
                val messageType = if (user.isFirstMeet) PokeMessageType.POKE_SOMEONE else PokeMessageType.POKE_FRIEND

                if (fragmentActivity != null && fragmentManager != null) {
                    val bottomSheet = MessageListBottomSheetFragment.Builder()
                        .setMessageListType(messageType)
                        .onClickMessageListItem { message, isAnonymous ->
                            viewModel.pokeUser(
                                userId = user.userId,
                                isAnonymous = isAnonymous,
                                message = message,
                                isFirstMeet = user.isFirstMeet
                            )
                        }.create()

                    bottomSheet.show(fragmentManager, bottomSheetTag)
                }
            }
        })
    }

    val scrollListener = remember {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (lastVisibleItemPosition == totalItemCount - 1 && totalItemCount % 10 == 0) {
                    viewModel.getPokeNotification()
                }
            }
        }
    }

    LaunchedEffect(pokeNotificationUiState) {
        when (val state = pokeNotificationUiState) {
            is UiState.Loading -> {}
            is UiState.Success -> adapter.updatePokeNotification(state.data)
            is UiState.ApiError -> fragmentActivity?.showPokeToast(context.getString(R.string.toast_poke_error))
            is UiState.Failure -> fragmentActivity?.showPokeToast(state.throwable.message ?: context.getString(R.string.toast_poke_error))
        }
    }

    LaunchedEffect(pokeUserUiState) {
        binding?.let { currentBinding ->
            when (val state = pokeUserUiState) {
                is UiState.Loading -> {}
                is UiState.Success<PokeUser> -> {
                    dismissBottomSheet(
                        fragmentManager = fragmentManager,
                        bottomSheetTag = bottomSheetTag
                    )

                    adapter.updatePokeUserItemPokeState(state.data.userId)
                    val user = state.data

                    if (state.isFirstMeet && !user.isFirstMeet) {
                        if (currentBinding.layoutLottie.visibility != View.VISIBLE) {
                            currentBinding.layoutLottie.visibility = View.VISIBLE
                            currentBinding.tvLottie.text = context.getString(
                                R.string.friend_complete,
                                if (user.isAnonymous) user.anonymousName else user.name
                            )
                            currentBinding.animationViewLottie.playAnimation()
                        }
                    } else {
                        val isBestFriend = isBestFriend(user.pokeNum, user.isAnonymous)
                        val isSoulMate = isSoulMate(user.pokeNum, user.isAnonymous)

                        if (!isBestFriend && !isSoulMate) {
                            if (!state.isFirstMeet) {
                                fragmentActivity?.showPokeToast(context.getString(R.string.toast_poke_user_success))
                            }
                        } else {
                            if (!currentBinding.animationFriendViewLottie.isAnimating) {
                                if (isBestFriend) {
                                    currentBinding.layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                    currentBinding.tvFreindLottie.text = context.getString(R.string.anonymous_to_friend, user.anonymousName, "단짝친구가")
                                    currentBinding.tvFreindLottieHint.text = context.getString(R.string.anonymous_user_info_part, user.generation, user.part)
                                    currentBinding.animationFriendViewLottie.setAnimation(R.raw.friendtobestfriend)
                                    currentBinding.animationFriendViewLottie.playAnimation()
                                } else if (isSoulMate) {
                                    viewModel.setAnonymousFriend(user)
                                    currentBinding.layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                    currentBinding.tvFreindLottie.text = context.getString(R.string.anonymous_to_friend, user.anonymousName, "천생연분이")
                                    currentBinding.animationFriendViewLottie.setAnimation(R.raw.bestfriendtosoulmate)
                                    currentBinding.animationFriendViewLottie.playAnimation()
                                }
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
        factory = ActivityPokeNotificationBinding::inflate,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        binding = this
        if (recyclerviewPokeNotification.adapter == null) {
            includeAppBar.textViewTitle.text = root.context.getString(R.string.poke_title)

            recyclerviewPokeNotification.adapter = adapter

            recyclerviewPokeNotification.addOnScrollListener(scrollListener)

            animationViewLottie.addOnAnimationEndListener {
                layoutLottie.visibility = View.GONE
            }

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

            layoutLottie.setOnClickListener { }
        }
    }
}
