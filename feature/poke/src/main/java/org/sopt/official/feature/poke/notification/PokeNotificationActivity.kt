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
package org.sopt.official.feature.poke.notification

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.viewBinding
import org.sopt.official.di.SoptViewModelFactory
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeNotificationBinding
import org.sopt.official.feature.poke.main.PokeMainActivity
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.feature.poke.util.setRelationStrokeColor
import org.sopt.official.feature.poke.util.showPokeToast

@Deprecated("PokeNotificationScreen으로 대체")
@Inject
class PokeNotificationActivity(
    private val viewModelFactory: SoptViewModelFactory,
    private val tracker: Tracker
) : AppCompatActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    private val binding by viewBinding(ActivityPokeNotificationBinding::inflate)
    private val viewModel by viewModels<PokeNotificationViewModel>()

    private val args by serializableExtra(Argument(""))

    private val pokeNotificationAdapter
        get() = binding.recyclerviewPokeNotification.adapter as PokeNotificationAdapter

    private val pokeNotificationLayoutManager
        get() = binding.recyclerviewPokeNotification.layoutManager as LinearLayoutManager

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge()
        initAppBar()
        initListener()
        initRecyclerView()
        initStateFlowValues()
    }

    override fun onResume() {
        super.onResume()
        tracker.track(
            EventType.VIEW,
            name = "view_poke_alarm_detail",
            properties = mapOf("view_type" to intent.getStringExtra("userStatus")),
        )
        initLottieView()
    }

    private fun initAppBar() {
        binding.includeAppBar.apply {
            textViewTitle.text = getString(R.string.poke_notification_title)
            toolbar.setOnClickListener { finish() }
        }
    }

    private fun initListener() {
        with(binding) {
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

    private val pokeUserListClickLister = object : PokeUserListClickListener {
        override fun onClickProfileImage(userId: Int) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, userId))))
            tracker.track(
                type = EventType.CLICK,
                name = "memberprofile",
                properties = mapOf(
                    "view_type" to args?.userStatus,
                    "click_view_type" to "poke_alarm",
                    "view_profile" to userId,
                ),
            )
        }

        override fun onClickPokeButton(user: PokeUser) {
            val messageType = when (user.isFirstMeet) {
                true -> PokeMessageType.POKE_SOMEONE
                false -> PokeMessageType.POKE_FRIEND
            }
            
            val bottomSheet = supportFragmentManager.fragmentFactory.instantiate(
                MessageListBottomSheetFragment::class.java.classLoader!!,
                MessageListBottomSheetFragment::class.java.name
            ) as MessageListBottomSheetFragment

            bottomSheet.arguments = android.os.Bundle().apply {
                putSerializable("pokeMessageType", messageType)
            }

            bottomSheet.onClickMessageListItem = { message, isAnonymous ->
                viewModel.pokeUser(
                    userId = user.userId, isAnonymous = isAnonymous, message = message, isFirstMeet = user.isFirstMeet
                )
            }

            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            
            tracker.track(
                type = EventType.CLICK,
                name = "poke_icon",
                properties = mapOf(
                    "view_type" to args?.userStatus,
                    "click_view_type" to "poke_alarm",
                    "view_profile" to user.userId,
                ),
            )
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisibleItemPosition = pokeNotificationLayoutManager.findLastVisibleItemPosition()
            val totalItemCount = pokeNotificationLayoutManager.itemCount
            if (lastVisibleItemPosition == totalItemCount - 1 && totalItemCount % 10 == 0) {
                viewModel.getPokeNotification()
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.recyclerviewPokeNotification) {
            adapter = PokeNotificationAdapter(pokeUserListClickLister)
            addOnScrollListener(scrollListener)
        }
    }

    private fun initStateFlowValues() {
        viewModel.pokeNotification.onEach {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Success<List<PokeUser>> -> pokeNotificationAdapter.updatePokeNotification(it.data)
                is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
            }
        }.launchIn(lifecycleScope)

        viewModel.pokeUserUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Success<PokeUser> -> {
                    messageListBottomSheet?.dismiss()
                    pokeNotificationAdapter.updatePokeUserItemPokeState(it.data.userId)
                    when (it.isFirstMeet && !it.data.isFirstMeet) {
                        true -> {
                            with(binding) {
                                layoutLottie.visibility = View.VISIBLE
                                tvLottie.text = binding.root.context.getString(
                                    R.string.friend_complete, if (it.data.isAnonymous) it.data.anonymousName else it.data.name
                                )
                                animationViewLottie.playAnimation()
                            }
                        }

                        false -> {
                            if (PokeMainActivity.isBestFriend(it.data.pokeNum, it.data.isAnonymous)) {
                                with(binding) {
                                    layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                    tvFreindLottie.text = getString(R.string.anonymous_to_friend, it.data.anonymousName, "단짝친구가")
                                    tvFreindLottieHint.text = getString(R.string.anonymous_user_info_part, it.data.generation, it.data.part)
                                    animationFriendViewLottie.apply {
                                        setAnimation(R.raw.friendtobestfriend)
                                    }.playAnimation()
                                }
                            } else if (PokeMainActivity.isSoulMate(it.data.pokeNum, it.data.isAnonymous)) {
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
        }.launchIn(lifecycleScope)
    }

    private fun initLottieView() {
        with(binding) {
            layoutLottie.visibility = View.GONE
            layoutAnonymousFriendOpen.visibility = View.GONE
            layoutAnonymousFriendLottie.visibility = View.GONE
        }
    }

    data class Argument(
        val userStatus: String,
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: Argument) = Intent(context, PokeNotificationActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}