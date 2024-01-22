/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.analytics.AmplitudeTracker
import org.sopt.official.analytics.EventType
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.common.util.viewBinding
import org.sopt.official.domain.poke.entity.PokeFriendOfFriendList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeMainBinding
import org.sopt.official.feature.poke.friend_list_summary.FriendListSummaryActivity
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.notification.PokeNotificationActivity
import org.sopt.official.feature.poke.onboarding.OnboardingActivity
import org.sopt.official.feature.poke.util.setRelationStrokeColor
import org.sopt.official.feature.poke.util.showPokeToast
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class PokeMainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityPokeMainBinding::inflate)
    private val viewModel by viewModels<PokeMainViewModel>()

    private val args by serializableExtra(StartArgs(""))

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    @Inject
    lateinit var tracker: AmplitudeTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()
        initListener()
        initFriendOfFriendEmptyViewText()
        initStateFlowValues()
    }

    override fun onResume() {
        super.onResume()
        tracker.track(type = EventType.VIEW, name = "poke_main", properties = mapOf("view_type" to args?.userStatus))
    }

    private fun initData() {
        viewModel.getPokeMe()
        viewModel.getPokeFriend()
        viewModel.getPokeFriendOfFriend()
    }

    private fun initListener() {
        with(binding) {
            includeAppBar.toolbar.setOnClickListener {
                tracker.track(type = EventType.CLICK, name = "poke_quit")
                finish()
            }

            btnNextSomeonePokeMe.setOnClickListener {
                tracker.track(type = EventType.CLICK, name = "poke_alarm_detail", properties = mapOf("view_type" to args?.userStatus))
                PokeNotificationActivity.getIntent(
                    this@PokeMainActivity,
                    PokeNotificationActivity.StartArgs(args?.userStatus ?: UserStatus.UNAUTHENTICATED.name)
                )
            }

            imgNextPokeMyFriend.setOnClickListener {
                startActivity(
                    FriendListSummaryActivity.getIntent(
                        this@PokeMainActivity,
                        FriendListSummaryActivity.StartArgs(args?.userStatus ?: UserStatus.UNAUTHENTICATED.name)
                    )
                )
            }

            scrollviewPokeMain.viewTreeObserver.addOnScrollChangedListener {
                refreshLayoutPokeMain.isEnabled = binding.scrollviewPokeMain.scrollY == 0
            }

            refreshLayoutPokeMain.setOnRefreshListener {
                initData()
                refreshLayoutPokeMain.isRefreshing = false
            }

            animationViewLottie.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    layoutLottie.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })

            layoutLottie.setOnClickListener {
                // do nothing
            }
        }
    }

    private fun initFriendOfFriendEmptyViewText() {
        binding.apply {
            val emptyViewText = getString(R.string.poke_my_friend_of_friend_empty)
            includeFriendListEmptyView01.textView.text = emptyViewText
            includeFriendListEmptyView02.textView.text = emptyViewText
        }
    }

    private fun initStateFlowValues() {
        viewModel.pokeMeUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> initPokeMeView(it.data)
                    is UiState.ApiError -> binding.layoutSomeonePokeMe.setVisible(false)
                    is UiState.Failure -> binding.layoutSomeonePokeMe.setVisible(false)
                }
            }
            .launchIn(lifecycleScope)

        viewModel.pokeFriendUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> initPokeFriendView(it.data)
                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }
            .launchIn(lifecycleScope)

        viewModel.apply {
            pokeFriendOfFriendUiState
                .onEach {
                    when (it) {
                        is UiState.Loading -> "Loading"
                        is UiState.Success<List<PokeFriendOfFriendList>> -> {
                            setPokeFriendOfFriendVisible(it.data)
                            initPokeFriendOfFriendView(it.data)
                        }

                        is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                        is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                    }
                }
                .launchIn(lifecycleScope)
        }

        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        viewModel.updatePokeUserState(it.data.userId)
                        when (it.isFirstMeet && !it.data.isFirstMeet) {
                            true -> {
                                binding.layoutLottie.visibility = View.VISIBLE
                                binding.tvLottie.text = binding.root.context.getString(R.string.friend_complete, it.data.name)
                                binding.animationViewLottie.playAnimation()
                            }

                            false -> showPokeToast(getString(R.string.toast_poke_user_success))
                        }
                    }

                    is UiState.ApiError -> {
                        messageListBottomSheet?.dismiss()
                        showPokeToast(getString(R.string.poke_user_alert_exceeded))
                    }

                    is UiState.Failure -> {
                        messageListBottomSheet?.dismiss()
                        showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initPokeMeView(pokeMeItem: PokeUser) {
        with(binding) {
            layoutSomeonePokeMe.setVisible(true)
            imgUserProfileSomeonePokeMe.setOnClickListener {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties = mapOf(
                        "view_type" to args?.userStatus,
                        "click_view_type" to "poke_main_alarm",
                        "view_profile" to pokeMeItem.playgroundId
                    )
                )
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, pokeMeItem.playgroundId))))
            }
            pokeMeItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfileSomeonePokeMe.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfileSomeonePokeMe.setImageResource(R.drawable.ic_empty_profile)
            imgUserProfilePokeMeOutline.setRelationStrokeColor(pokeMeItem.relationName)
            tvUserNameSomeonePokeMe.text = pokeMeItem.name
            tvUserGenerationSomeonePokeMe.text = "${pokeMeItem.generation}기 ${pokeMeItem.part}"
            tvUserMsgSomeonePokeMe.text = pokeMeItem.message
            tvFriendsStatusSomeonePokeMe.text = if (pokeMeItem.isFirstMeet) {
                pokeMeItem.mutualRelationMessage
            } else {
                "${pokeMeItem.relationName} ${pokeMeItem.pokeNum}콕"
            }
            btnSomeonePokeMe.isEnabled = !pokeMeItem.isAlreadyPoke
            btnSomeonePokeMe.setOnClickListener {
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_icon",
                    properties = mapOf(
                        "view_type" to args?.userStatus,
                        "click_view_type" to "poke_main_alarm",
                        "view_profile" to pokeMeItem.playgroundId
                    )
                )
                showMessageListBottomSheet(
                    pokeMeItem.userId,
                    when (pokeMeItem.isFirstMeet) {
                        true -> PokeMessageType.POKE_SOMEONE
                        false -> PokeMessageType.POKE_FRIEND
                    },
                    pokeMeItem.isFirstMeet
                )
            }
        }
    }

    private fun initPokeFriendView(pokeFriendItem: PokeUser) {
        with(binding) {
            imgUserProfilePokeMyFriend.setOnClickListener {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties = mapOf(
                        "view_type" to args?.userStatus,
                        "click_view_type" to "poke_main_friend",
                        "view_profile" to pokeFriendItem.playgroundId
                    )
                )
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, pokeFriendItem.playgroundId))))
            }
            pokeFriendItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfilePokeMyFriend.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfilePokeMyFriend.setImageResource(R.drawable.ic_empty_profile)
            imgUserProfilePokeMyFriendOutline.setRelationStrokeColor(pokeFriendItem.relationName)
            tvUserNamePokeMyFriend.text = pokeFriendItem.name
            tvUserGenerationPokeMyFriend.text = "${pokeFriendItem.generation}기 ${pokeFriendItem.part}"
            tvCountPokeMyFriend.text = "${pokeFriendItem.pokeNum}콕"
            btnPokeMyFriend.isEnabled = !pokeFriendItem.isAlreadyPoke
            btnPokeMyFriend.setOnClickListener {
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_icon",
                    properties = mapOf(
                        "view_type" to args?.userStatus,
                        "click_view_type" to "poke_main_friend",
                        "view_profile" to pokeFriendItem.playgroundId
                    )
                )
                showMessageListBottomSheet(pokeFriendItem.userId, PokeMessageType.POKE_FRIEND)
            }
        }
    }

    private fun setPokeFriendOfFriendVisible(list: List<PokeFriendOfFriendList>) {
        with(binding) {
            fun setVisibility(box1: View, box2: View, emptyBox: View, friendListSize: Int) {
                box1.visibility = if (friendListSize >= 1) View.VISIBLE else View.GONE
                box2.visibility = if (friendListSize == 2) View.VISIBLE else View.GONE
                emptyBox.visibility = if (friendListSize == 0) View.VISIBLE else View.GONE
            }

            when (list.size) {
                1 -> {
                    box2FriendOfFriend.visibility = View.GONE
                    val friendListSize = list[0].friendList.size
                    setVisibility(groupFriend1Box1, groupFriend2Box1, includeFriendListEmptyView01.root, friendListSize)
                }

                2 -> {
                    box2FriendOfFriend.visibility = View.VISIBLE
                    val friendListSize1 = list[0].friendList.size
                    val friendListSize2 = list[1].friendList.size

                    setVisibility(groupFriend1Box1, groupFriend2Box1, includeFriendListEmptyView01.root, friendListSize1)
                    setVisibility(groupFriend3Box2, groupFriend4Box2, includeFriendListEmptyView02.root, friendListSize2)
                }
            }
        }
    }

    private fun initPokeFriendOfFriendView(list: List<PokeFriendOfFriendList>) {
        with(binding) {
            val myFriendNameTextViews = listOf(tvMyFriendName1, tvMyFriendName2)
            val myFriendProfileImageViews = listOf(imgMyFriendProfile1, imgMyFriendProfile2)

            val friendProfileImageViews = listOf(
                imgFriendProfile1OfMyFriend, imgFriendProfile2OfMyFriend,
                imgFriendProfile3OfMyFriend, imgFriendProfile4OfMyFriend
            )
            val friendTextViews = listOf(
                tvFriendName1OfMyFriend, tvFriendName2OfMyFriend,
                tvFriendName3OfMyFriend, tvFriendName4OfMyFriend
            )
            val friendGenerationTextViews = listOf(
                tvFriendGeneration1OfMyFriend, tvFriendGeneration2OfMyFriend,
                tvFriendGeneration3OfMyFriend, tvFriendGeneration4OfMyFriend
            )
            val btnPokeImageViews = listOf(
                btnFriendPoke1OfMyFriend, btnFriendPoke2OfMyFriend,
                btnFriendPoke3OfMyFriend, btnFriendPoke4OfMyFriend
            )

            list.take(2).forEachIndexed { index, friend ->
                myFriendNameTextViews[index].text = friend.friendName
                myFriendProfileImageViews[index].setOnClickListener {
                    tracker.track(
                        type = EventType.CLICK,
                        name = "memberprofile",
                        properties = mapOf(
                            "view_type" to args?.userStatus,
                            "click_view_type" to "poke_main_recommend_myfriend",
                            "view_profile" to friend.playgroundId
                        )
                    )
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, friend.playgroundId))))
                }
                friend.friendProfileImage.takeIf { it.isNotEmpty() }?.let {
                    myFriendProfileImageViews[index].load(it) { transformations(CircleCropTransformation()) }
                } ?: run {
                    myFriendProfileImageViews[index].setImageResource(R.drawable.ic_empty_profile)
                }

                (0 until 2).forEach { friendIndex ->
                    val friendOfFriend = friend.friendList.getOrNull(friendIndex)
                    val friendProfileImageView = friendProfileImageViews[2 * index + friendIndex]
                    val friendNameTextView = friendTextViews[2 * index + friendIndex]
                    val friendGenerationTextView = friendGenerationTextViews[2 * index + friendIndex]
                    val btnPokeImageView = btnPokeImageViews[2 * index + friendIndex]

                    friendOfFriend?.let { myFriendOfFriend ->
                        friendProfileImageView.setOnClickListener {
                            tracker.track(
                                type = EventType.CLICK,
                                name = "memberprofile",
                                properties = mapOf(
                                    "view_type" to args?.userStatus,
                                    "click_view_type" to "poke_main_recommend_notmyfriend",
                                    "view_profile" to friend.playgroundId
                                )
                            )
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(getString(R.string.poke_user_profile_url, myFriendOfFriend.playgroundId))
                                )
                            )
                        }
                        myFriendOfFriend.profileImage.takeIf { it.isNotEmpty() }?.let { url ->
                            friendProfileImageView.load(url) { transformations(CircleCropTransformation()) }
                        } ?: run {
                            friendProfileImageView.setImageResource(R.drawable.ic_empty_profile)
                        }
                        friendNameTextView.text = myFriendOfFriend.name
                        friendGenerationTextView.text = "${myFriendOfFriend.generation}기 ${myFriendOfFriend.part}"
                        btnPokeImageView.isEnabled = !myFriendOfFriend.isAlreadyPoke
                        btnPokeImageView.setOnClickListener {
                            tracker.track(
                                type = EventType.CLICK,
                                name = "poke_icon",
                                properties = mapOf(
                                    "view_type" to args?.userStatus,
                                    "click_view_type" to "poke_main_recommend_notmyfriend",
                                    "view_profile" to myFriendOfFriend.playgroundId
                                )
                            )
                            showMessageListBottomSheet(myFriendOfFriend.userId, PokeMessageType.POKE_SOMEONE)
                        }
                    }
                }
            }
        }
    }

    private fun showMessageListBottomSheet(userId: Int, pokeMessageType: PokeMessageType, isFirstMeet: Boolean = false) {
        messageListBottomSheet = MessageListBottomSheetFragment.Builder()
            .setMessageListType(pokeMessageType)
            .onClickMessageListItem { message -> viewModel.pokeUser(userId, message, isFirstMeet) }
            .create()

        messageListBottomSheet?.let {
            it.show(supportFragmentManager, it.tag)
        }
    }

    data class StartArgs(
        val userStatus: String
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) =
            Intent(context, OnboardingActivity::class.java).apply {
                putExtra("args", args)
            }
    }
}
