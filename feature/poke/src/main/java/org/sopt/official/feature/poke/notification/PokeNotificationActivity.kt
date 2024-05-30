/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.analytics.AmplitudeTracker
import org.sopt.official.analytics.EventType
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.viewBinding
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeNotificationBinding
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.util.showPokeToast
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class PokeNotificationActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPokeNotificationBinding::inflate)
    private val viewModel by viewModels<PokeNotificationViewModel>()

    private val args by serializableExtra(StartArgs(""))

    @Inject
    lateinit var tracker: AmplitudeTracker
    private val pokeNotificationAdapter
        get() = binding.recyclerviewPokeNotification.adapter as PokeNotificationAdapter

    private val pokeNotificationLayoutManager
        get() = binding.recyclerviewPokeNotification.layoutManager as LinearLayoutManager

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
    }

    private fun initAppBar() {
        binding.includeAppBar.apply {
            textViewTitle.text = getString(R.string.poke_notification_title)
            toolbar.setOnClickListener { finish() }
        }
    }

    private fun initListener() {
        with(binding) {
            animationViewLottie.addAnimatorListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        layoutLottie.visibility = View.GONE
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                },
            )

            layoutLottie.setOnClickListener {
                // do nothing
            }
        }
    }

    private val pokeUserListClickLister =
        object : PokeUserListClickListener {
            override fun onClickProfileImage(playgroundId: Int) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, playgroundId))))
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties =
                    mapOf(
                        "view_type" to args?.userStatus,
                        "click_view_type" to "poke_alarm",
                        "view_profile" to playgroundId,
                    ),
                )
            }

            override fun onClickPokeButton(user: PokeUser) {
                val messageType =
                    when (user.isFirstMeet) {
                        true -> PokeMessageType.POKE_SOMEONE
                        false -> PokeMessageType.POKE_FRIEND
                    }
                messageListBottomSheet =
                    MessageListBottomSheetFragment.Builder()
                        .setMessageListType(messageType)
                        .onClickMessageListItem { message ->
                            viewModel.pokeUser(
                                userId = user.userId,
                                isAnonymous = false,
                                message = message,
                                isFirstMeet = user.isFirstMeet
                            )
                        }
                        .create()

                messageListBottomSheet?.let {
                    it.show(supportFragmentManager, it.tag)
                }
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_icon",
                    properties =
                    mapOf(
                        "view_type" to args?.userStatus,
                        "click_view_type" to "poke_alarm",
                        "view_profile" to user.playgroundId,
                    ),
                )
            }
        }

    private val scrollListener =
        object : RecyclerView.OnScrollListener() {
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
        viewModel.pokeNotification
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<List<PokeUser>> -> pokeNotificationAdapter.updatePokeNotification(it.data)
                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }
            .launchIn(lifecycleScope)

        viewModel.pokeUserUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        pokeNotificationAdapter.updatePokeUserItemPokeState(it.data.userId)
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

    data class StartArgs(
        val userStatus: String,
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, PokeNotificationActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}
