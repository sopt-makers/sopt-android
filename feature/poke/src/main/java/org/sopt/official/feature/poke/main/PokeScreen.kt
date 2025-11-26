
package org.sopt.official.feature.poke.main

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
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeMainBinding
import org.sopt.official.feature.poke.friend.summary.FriendListSummaryActivity
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
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
    val coroutineScope = rememberCoroutineScope()
    val activity = context as FragmentActivity

    val binding = remember {
        ActivityPokeMainBinding.inflate(LayoutInflater.from(context))
    }

    val pokeUiState by viewModel.pokeMeUiState.collectAsStateWithLifecycle()
    val pokeFriendUiState by viewModel.pokeFriendUiState.collectAsStateWithLifecycle()
    val pokeSimilarFriendUiState by viewModel.pokeSimilarFriendUiState.collectAsStateWithLifecycle()

    val pokeMainListAdapter = remember {
        PokeMainListAdapter(object : PokeUserListClickListener {
            override fun onClickProfileImage(userId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties = mapOf("view_type" to userStatus.name, "click_view_type" to "onboarding", "view_profile" to userId),
                )
                context.startActivity(Intent(Intent.ACTION_VIEW, context.getString(R.string.poke_user_profile_url, userId).toUri()))
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
                showMessageListBottomSheet(activity, viewModel, user.userId, PokeMessageType.POKE_SOMEONE)
            }
        })
    }

    LaunchedEffect(Unit) {
        binding.recyclerViewPokeMain.adapter = pokeMainListAdapter

        viewModel.getPokeMe()
        viewModel.getPokeFriend()
        viewModel.getPokeSimilarFriends()
    }

    LaunchedEffect(pokeUiState) {
        when (val state = pokeUiState) {
            is UiState.Loading -> {}
            is UiState.Success<PokeUser> -> initPokeMeView(binding, state.data, tracker, userStatus, activity, viewModel)
            is UiState.ApiError -> binding.layoutSomeonePokeMe.setVisible(false)
            is UiState.Failure -> binding.layoutSomeonePokeMe.setVisible(false)
        }
    }

    LaunchedEffect(pokeFriendUiState) {
        when (val state = pokeFriendUiState) {
            is UiState.Loading -> {}
            is UiState.Success<PokeUser> -> initPokeFriendView(binding, state.data, tracker, userStatus, activity, viewModel)
            is UiState.ApiError -> activity.showPokeToast(context.getString(R.string.toast_poke_error))
            is UiState.Failure -> activity.showPokeToast(state.throwable.message ?: context.getString(R.string.toast_poke_error))
        }
    }

    LaunchedEffect(pokeSimilarFriendUiState) {
        when (val state = pokeSimilarFriendUiState) {
            is UiState.Loading -> {}
            is UiState.Success<List<PokeRandomUserList.PokeRandomUsers>> -> {
                pokeMainListAdapter.submitList(state.data)
                binding.refreshLayoutPokeMain.isRefreshing = false
            }
            is UiState.ApiError -> activity.showPokeToast(context.getString(R.string.toast_poke_error))
            is UiState.Failure -> activity.showPokeToast(state.throwable.message ?: context.getString(R.string.toast_poke_error))
        }
    }

    LifecycleResumeEffect(Unit) {
        tracker.track(
            type = EventType.VIEW,
            name = "poke_main",
            properties = mapOf("view_type" to userStatus.name)
        )

        with(binding) {
            layoutLottie.visibility = View.GONE
            layoutAnonymousFriendOpen.visibility = View.GONE
            layoutAnonymousFriendLottie.visibility = View.GONE
        }

        onPauseOrDispose {}
    }

    AndroidView(
        factory = { _ ->
            with(binding) {
                /*includeAppBar.toolbar.setOnClickListener {
                    tracker.track(type = EventType.CLICK, name = "poke_quit")
                }*/

                btnNextSomeonePokeMe.setOnClickListener {
                    tracker.track(type = EventType.CLICK, name = "poke_alarm_detail", properties = mapOf("view_type" to userStatus.name))
                    /*context.startActivity(
                        PokeNotificationActivity.getIntent(
                            context,
                            PokeNotificationActivity.Argument(userStatus.name),
                        ),
                    )*/
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
                    refreshLayoutPokeMain.isEnabled = binding.scrollviewPokeMain.scrollY == 0
                }

                refreshLayoutPokeMain.setOnRefreshListener {
                    viewModel.getPokeMe()
                    viewModel.getPokeFriend()
                    viewModel.getPokeSimilarFriends()
                    refreshLayoutPokeMain.isRefreshing = false
                }

                animationViewLottie.addOnAnimationEndListener {
                    layoutLottie.visibility = View.GONE
                }

                animationFriendViewLottie.addOnAnimationEndListener {
                    if (viewModel.anonymousFriend.value != null) { // 천생연분 -> 정체 공개
                        coroutineScope.launch {
                            // 로티
                            layoutAnonymousFriendLottie.visibility = View.GONE
                            layoutAnonymousFriendOpen.visibility = View.VISIBLE

                            val anonymousFriend = viewModel.anonymousFriend.value
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

                layoutLottie.setOnClickListener {
                    // do nothing
                }
            }
            binding.root
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        update = {}
    )
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
                properties = mapOf("view_type" to userStatus.name, "click_view_type" to "poke_main_alarm", "view_profile" to pokeMeItem.userId),
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