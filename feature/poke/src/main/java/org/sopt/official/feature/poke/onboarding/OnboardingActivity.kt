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
package org.sopt.official.feature.poke.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dev.zacsweers.metro.Inject
import java.io.Serializable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.viewBinding
import org.sopt.official.di.SoptViewModelFactory
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityOnboardingBinding
import org.sopt.official.feature.poke.main.PokeMainActivity
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.feature.poke.util.showPokeToast

@Deprecated("OnboardingScreen으로 대체")
@Inject
class OnboardingActivity(
    private val viewModelFactory: SoptViewModelFactory,
    private val tracker: Tracker
) : AppCompatActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    private val binding by viewBinding(ActivityOnboardingBinding::inflate)
    private val viewModel by viewModels<OnboardingViewModel>()

    private val args by serializableExtra(StartArgs(0, ""))

    private var onboardingBottomSheet: OnboardingBottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge()
        initAppBar()
        initLottieAnimatorListener()
        launchCheckNewInPokeOnboardingStateFlow()
        launchOnboardingPokeUserListUiStateFlow()
    }

    override fun onResume() {
        super.onResume()
        tracker.track(type = EventType.VIEW, name = "poke_onboarding", properties = mapOf("view_type" to args?.userStatus))
    }

    private fun initAppBar() {
        /*with(binding.includeAppBar) {
            textViewTitle.text = getString(R.string.poke_title)
        }*/
    }

    private fun initLottieAnimatorListener() {
        with(binding) {
            animationViewLottie.addOnAnimationEndListener {
                layoutLottie.visibility = View.GONE
                setIntentToPokeMain()
            }

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
            onboardingBottomSheet =
                OnboardingBottomSheetFragment.Builder()
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
                    is UiState.Loading -> {}
                    is UiState.Success<PokeRandomUserList> -> {}//updateRecyclerView(it.data)
                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }
            .launchIn(lifecycleScope)
    }

    /*private fun updateRecyclerView(data: PokeRandomUserList) {
        with(binding) {
            viewpager.adapter = OnboardingViewPagerAdapter(
                this@OnboardingActivity,
                data,
                args
            )
            dotsIndicator.attachTo(binding.viewpager)
        }
    }*/

    private fun setIntentToPokeMain() {
        if (binding.layoutLottie.isVisible) return
        startActivity(Intent(this, PokeMainActivity::class.java))
        finish()
    }

    fun playLottieAnimation(userName: String) {
        with(binding) {
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
        fun getIntent(context: Context, args: StartArgs) = Intent(context, OnboardingActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}