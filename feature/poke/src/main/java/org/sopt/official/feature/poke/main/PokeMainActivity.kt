/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import dev.zacsweers.metro.Inject
import java.io.Serializable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.model.UserStatus
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.common.util.viewBinding
import org.sopt.official.di.SoptViewModelFactory
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeMainBinding
import org.sopt.official.feature.poke.friend.summary.FriendListSummaryActivity
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.notification.PokeNotificationActivity
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.feature.poke.util.setRelationStrokeColor
import org.sopt.official.feature.poke.util.showPokeToast

@Deprecated("PokeScreen으로 대체")
@Inject
class PokeMainActivity(
    private val viewModelFactory: SoptViewModelFactory,
    private val tracker: Tracker
) : AppCompatActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    private val binding by viewBinding(ActivityPokeMainBinding::inflate)
    private val viewModel by viewModels<PokeMainViewModel>()

    private val args by serializableExtra(StartArgs(""))

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    private val pokeMainListAdapter
        get() = binding.recyclerViewPokeMain.adapter as PokeMainListAdapter?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge()
        initAdapter()
        initData()
        initListener()
        initStateFlowValues()
    }

    override fun onResume() {
        super.onResume()
        tracker.track(type = EventType.VIEW, name = "poke_main", properties = mapOf("view_type" to args?.userStatus))
        initLottieView()
    }

    private fun initAdapter() {
        binding.recyclerViewPokeMain.adapter = PokeMainListAdapter(pokeUserListClickLister)
    }

    private fun initData() {
        viewModel.getPokeMe()
        viewModel.getPokeFriend()
        viewModel.getPokeSimilarFriends()
    }

    private fun initListener() {
        with(binding) {
            /*includeAppBar.toolbar.setOnClickListener {
                tracker.track(type = EventType.CLICK, name = "poke_quit")
                finish()
            }*/

            btnNextSomeonePokeMe.setOnClickListener {
                tracker.track(type = EventType.CLICK, name = "poke_alarm_detail", properties = mapOf("view_type" to args?.userStatus))
                startActivity(
                    PokeNotificationActivity.getIntent(
                        this@PokeMainActivity,
                        PokeNotificationActivity.Argument(args?.userStatus ?: UserStatus.UNAUTHENTICATED.name),
                    ),
                )
            }

            imgNextPokeMyFriend.setOnClickListener {
                startActivity(
                    FriendListSummaryActivity.getIntent(
                        this@PokeMainActivity,
                        FriendListSummaryActivity.StartArgs(args?.userStatus ?: UserStatus.UNAUTHENTICATED.name),
                    ),
                )
            }

            scrollviewPokeMain.viewTreeObserver.addOnScrollChangedListener {
                refreshLayoutPokeMain.isEnabled = binding.scrollviewPokeMain.scrollY == 0
            }

            refreshLayoutPokeMain.setOnRefreshListener {
                initData()
                refreshLayoutPokeMain.isRefreshing = false
            }

            animationViewLottie.addOnAnimationEndListener {
                layoutLottie.visibility = View.GONE
            }

            animationFriendViewLottie.addOnAnimationEndListener {
                if (viewModel.anonymousFriend.value != null) { // 천생연분 -> 정체 공개
                    lifecycleScope.launch {
                        // 로티
                        layoutAnonymousFriendLottie.visibility = View.GONE
                        layoutAnonymousFriendOpen.visibility = View.VISIBLE

                        val anonymousFriend = viewModel.anonymousFriend.value
                        anonymousFriend?.let {
                            tvAnonymousFreindName.text = getString(R.string.anonymous_user_identity, it.anonymousName)
                            tvAnonymousFreindInfo.text = getString(R.string.anonymous_user_info, it.generation, it.part, it.name)
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
    }

    private fun initStateFlowValues() {
        viewModel.pokeMeUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success<PokeUser> -> initPokeMeView(it.data)
                    is UiState.ApiError -> binding.layoutSomeonePokeMe.setVisible(false)
                    is UiState.Failure -> binding.layoutSomeonePokeMe.setVisible(false)
                }
            }
            .launchIn(lifecycleScope)

        viewModel.pokeFriendUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success<PokeUser> -> initPokeFriendView(it.data)
                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }
            .launchIn(lifecycleScope)

        viewModel.pokeSimilarFriendUiState.onEach {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Success<List<PokeRandomUserList.PokeRandomUsers>> -> {
                    pokeMainListAdapter?.submitList(it.data)
                    binding.refreshLayoutPokeMain.isRefreshing = false
                }

                is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
            }
        }.launchIn(lifecycleScope)

        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        viewModel.updatePokeUserState(it.data.userId)
                        when (it.isFirstMeet && !it.data.isFirstMeet) { // 친구
                            true -> {
                                with(binding) {
                                    layoutLottie.visibility = View.VISIBLE
                                    tvLottie.text = binding.root.context.getString(
                                        R.string.friend_complete,
                                        if (it.data.isAnonymous) it.data.anonymousName else it.data.name
                                    )
                                    animationViewLottie.playAnimation()
                                }
                            }

                            false -> {
                                if (isBestFriend(it.data.pokeNum, it.data.isAnonymous)) {
                                    with(binding) {
                                        layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                        tvFreindLottie.text = getString(R.string.anonymous_to_friend, it.data.anonymousName, "단짝친구가")
                                        tvFreindLottieHint.text =
                                            getString(R.string.anonymous_user_info_part, it.data.generation, it.data.part)
                                        animationFriendViewLottie.apply {
                                            setAnimation(R.raw.friendtobestfriend)
                                        }.playAnimation()
                                    }
                                } else if (isSoulMate(it.data.pokeNum, it.data.isAnonymous)) {
                                    viewModel.setAnonymousFriend(it.data)
                                    with(binding) {
                                        layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                        tvFreindLottie.text = getString(R.string.anonymous_to_friend, it.data.anonymousName, "천생연분이")
                                        animationFriendViewLottie.apply {
                                            setAnimation(R.raw.bestfriendtosoulmate)
                                        }.playAnimation()
                                    }
                                } else {
                                    showPokeToast(getString(R.string.toast_poke_user_success))
                                }
                            }
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
                if (pokeMeItem.isAnonymous) return@setOnClickListener

                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties =
                        mapOf(
                            "view_type" to args?.userStatus,
                            "click_view_type" to "poke_main_alarm",
                            "view_profile" to pokeMeItem.userId,
                        ),
                )
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, pokeMeItem.userId))))
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
                tvUserGenerationSomeonePokeMe.text = getString(R.string.poke_user_info, pokeMeItem.generation, pokeMeItem.part)
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
                            "view_type" to args?.userStatus,
                            "click_view_type" to "poke_main_alarm",
                            "view_profile" to pokeMeItem.userId,
                        ),
                )
                showMessageListBottomSheet(
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

    private fun initPokeFriendView(pokeFriendItem: PokeUser) {
        with(binding) {
            imgUserProfilePokeMyFriend.setOnClickListener {
                if (pokeFriendItem.isAnonymous) return@setOnClickListener

                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties =
                        mapOf(
                            "view_type" to args?.userStatus,
                            "click_view_type" to "poke_main_friend",
                            "view_profile" to pokeFriendItem.userId,
                        ),
                )
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, pokeFriendItem.userId))))
            }

            if (pokeFriendItem.isAnonymous) {
                imgUserProfilePokeMyFriend.load(pokeFriendItem.anonymousImage) { transformations(CircleCropTransformation()) }
                tvUserNamePokeMyFriend.text = pokeFriendItem.anonymousName
            } else {
                pokeFriendItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                    imgUserProfilePokeMyFriend.load(it) { transformations(CircleCropTransformation()) }
                } ?: imgUserProfilePokeMyFriend.setImageResource(R.drawable.ic_empty_profile)
                tvUserNamePokeMyFriend.text = pokeFriendItem.name
                tvUserGenerationSomeonePokeMe.text = getString(R.string.poke_user_info, pokeFriendItem.generation, pokeFriendItem.part)
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
                            "view_type" to args?.userStatus,
                            "click_view_type" to "poke_main_friend",
                            "view_profile" to pokeFriendItem.userId,
                        ),
                )
                showMessageListBottomSheet(pokeFriendItem.userId, PokeMessageType.POKE_FRIEND)
            }
        }
    }

    private fun showMessageListBottomSheet(userId: Int, pokeMessageType: PokeMessageType, isFirstMeet: Boolean = false) {
        val bottomSheet = supportFragmentManager.fragmentFactory.instantiate(
            MessageListBottomSheetFragment::class.java.classLoader!!,
            MessageListBottomSheetFragment::class.java.name
        ) as MessageListBottomSheetFragment

        bottomSheet.arguments = android.os.Bundle().apply {
            putSerializable("pokeMessageType", pokeMessageType)
        }

        bottomSheet.onClickMessageListItem = { message, isAnonymous ->
            viewModel.pokeUser(
                userId = userId,
                isAnonymous = isAnonymous,
                message = message,
                isFirstMeet = isFirstMeet
            )
        }

        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun initLottieView() {
        with(binding) {
            layoutLottie.visibility = View.GONE
            layoutAnonymousFriendOpen.visibility = View.GONE
            layoutAnonymousFriendLottie.visibility = View.GONE
        }
    }

    private val pokeUserListClickLister =
        object : PokeUserListClickListener {
            override fun onClickProfileImage(userId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties = mapOf("view_type" to args?.userStatus, "click_view_type" to "onboarding", "view_profile" to userId),
                )
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, userId))))
            }

            override fun onClickPokeButton(user: PokeUser) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_icon",
                    properties =
                        mapOf(
                            "view_type" to args?.userStatus,
                            "click_view_type" to "onboarding",
                            "view_profile" to user.userId,
                        ),
                )

                val bottomSheet = supportFragmentManager.fragmentFactory.instantiate(
                    MessageListBottomSheetFragment::class.java.classLoader!!,
                    MessageListBottomSheetFragment::class.java.name
                ) as MessageListBottomSheetFragment

                bottomSheet.arguments = android.os.Bundle().apply {
                    putSerializable("pokeMessageType", PokeMessageType.POKE_SOMEONE)
                }

                bottomSheet.onClickMessageListItem = { message, isAnonymous ->
                    viewModel.pokeUser(
                        userId = user.userId,
                        isAnonymous = isAnonymous,
                        message = message,
                        isFirstMeet = false,
                    )
                }

                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }
        }

    data class StartArgs(
        val userStatus: String,
    ) : Serializable

    companion object {
        private val bestFriendRange = 5..6
        private val soulMateRange = 11..12
        fun isBestFriend(pokeNum: Int, isAnonymous: Boolean) = pokeNum in bestFriendRange && isAnonymous
        fun isSoulMate(pokeNum: Int, isAnonymous: Boolean) = pokeNum in soulMateRange && isAnonymous

        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, PokeMainActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}