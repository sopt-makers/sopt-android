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
package org.sopt.official.feature.poke.onboarding

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.analytics.AmplitudeTracker
import org.sopt.official.analytics.EventType
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.viewBinding
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.config.LocalTracker
import org.sopt.official.feature.poke.config.rememberTracker
import org.sopt.official.feature.poke.databinding.ActivityOnboardingBinding
import org.sopt.official.feature.poke.main.PokeEntryPointActivity
import org.sopt.official.feature.poke.message.MessagesBottomSheetFragment
import org.sopt.official.feature.poke.user.PokeUserListAdapter
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.user.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.showPokeToast
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityOnboardingBinding::inflate)
    private val viewModel by viewModels<OnboardingViewModel>()

    private val args by serializableExtra(StartArgs(0, ""))

    private var onboardingBottomSheet: OnboardingBottomSheetFragment? = null
    private var messageListBottomSheet: MessagesBottomSheetFragment? = null

    @Inject
    lateinit var tracker: AmplitudeTracker
    private val pokeUserListAdapter
        get() = binding.recyclerView.adapter as PokeUserListAdapter?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalTracker provides rememberTracker(LocalContext.current)
            ) {
                SoptTheme {
                    // TODO by Nunu Content 작성하기
                }
            }
        }
        // etContentView(binding.root)

        initAppBar()
        initView()
        initLottieAnimatorListener()

        launchCheckNewInPokeOnboardingStateFlow()
        launchOnboardingPokeUserListUiStateFlow()
        launchPokeUserUiStateFlow()
    }

    override fun onResume() {
        super.onResume()
        tracker.track(type = EventType.VIEW, name = "poke_onboarding", properties = mapOf("view_type" to args?.userStatus))
    }

    private fun initAppBar() {
        binding.includeAppBar.apply {
            toolbar.setOnClickListener { onBackPressed() }
            textViewTitle.text = getString(R.string.poke_title)
        }
    }

    private fun initView() {
        binding.apply {
            textViewContent.text = getString(R.string.onboarding_content, args?.currentGeneration)
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getOnboardingPokeUserList()
            }
            recyclerView.adapter = PokeUserListAdapter(
                pokeUserListItemViewType = PokeUserListItemViewType.LARGE,
                clickListener = pokeUserListClickLister,
            )
        }
    }

    private val pokeUserListClickLister = object : PokeUserListClickListener {
        override fun onClickProfileImage(playgroundId: Int) {
            tracker.track(
                type = EventType.CLICK,
                name = "memberprofile",
                properties = mapOf("view_type" to args?.userStatus, "click_view_type" to "onboarding", "view_profile" to playgroundId)
            )
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, playgroundId))))
        }

        override fun onClickPokeButton(user: PokeUser) {
            tracker.track(
                type = EventType.CLICK,
                name = "poke_icon",
                properties = mapOf("view_type" to args?.userStatus, "click_view_type" to "onboarding", "view_profile" to user.playgroundId)
            )
            messageListBottomSheet = MessagesBottomSheetFragment.Builder()
                .setMessageListType(PokeMessageType.POKE_SOMEONE)
                .onClickMessageListItem { message -> viewModel.pokeUser(user.userId, message) }
                .create()

            messageListBottomSheet?.let {
                it.show(supportFragmentManager, it.tag)
            }
        }
    }

    private fun initLottieAnimatorListener() {
        binding.apply {
            animationViewLottie.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    layoutLottie.visibility = View.GONE
                    setIntentToPokeMain()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            layoutLottie.setOnClickListener {
                // do nothing
            }
        }
    }

    private fun launchCheckNewInPokeOnboardingStateFlow() {
        viewModel.checkNewInPokeOnboardingState
            .onEach { if (it == true) showOnboardingBottomSheet() }
            .launchIn(lifecycleScope)
    }

    private fun showOnboardingBottomSheet() {
        if (onboardingBottomSheet?.isAdded == true) return
        if (onboardingBottomSheet == null) {
            onboardingBottomSheet = OnboardingBottomSheetFragment.Builder()
                .setOnDismissEvent { viewModel.updateNewInPokeOnboarding() }
                .create()
        }
        onboardingBottomSheet?.let {
            it.show(supportFragmentManager, it.tag)
        }
    }

    private fun launchOnboardingPokeUserListUiStateFlow() {
        viewModel.onboardingPokeUserListUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<List<PokeUser>> -> {
                        val newFriend = it.data.find { user -> user.pokeNum >= 2 }
                        when (newFriend == null) {
                            true -> updateRecyclerView(it.data)
                            false -> playLottieAnimation(newFriend.name)
                        }
                    }

                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun updateRecyclerView(data: List<PokeUser>) {
        pokeUserListAdapter?.submitList(data)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setIntentToPokeMain() {
        if (binding.layoutLottie.isVisible) return
        startActivity(Intent(this, PokeEntryPointActivity::class.java))
        finish()
    }

    private fun launchPokeUserUiStateFlow() {
        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        when (it.isFirstMeet && !it.data.isFirstMeet) {
                            true -> playLottieAnimation(it.data.name)
                            false -> showPokeToast(getString(R.string.toast_poke_user_success))
                        }
                    }

                    is UiState.ApiError -> {
                        messageListBottomSheet?.dismiss()
                        showPokeToast(getString(R.string.toast_poke_error))
                    }

                    is UiState.Failure -> {
                        messageListBottomSheet?.dismiss()
                        showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun playLottieAnimation(userName: String) {
        binding.apply {
            layoutLottie.visibility = View.VISIBLE
            tvLottie.text = root.context.getString(R.string.friend_complete, userName)
            animationViewLottie.playAnimation()
        }
    }

    data class StartArgs(
        val currentGeneration: Int,
        val userStatus: String,
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) =
            Intent(context, OnboardingActivity::class.java).apply {
                putExtra("args", args)
            }
    }
}
