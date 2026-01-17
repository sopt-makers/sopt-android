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
package org.sopt.official.feature.poke.onboarding

import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.FragmentActivity
import dev.zacsweers.metrox.viewmodel.metroViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.context.findActivity
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.common.di.SoptViewModelFactory
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.component.PokeErrorDialog
import org.sopt.official.feature.poke.databinding.ActivityOnboardingBinding
import org.sopt.official.feature.poke.onboarding.model.StartArgs
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.model.UserStatus


@Composable
fun OnboardingScreen(
    paddingValues: PaddingValues,
    userStatus: UserStatus,
    navigateToPokeMain: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: OnboardingViewModel = metroViewModel()
) {
    val context = LocalContext.current
    val tracker = LocalTracker.current
    val metroFactory = LocalMetroViewModelFactory.current as SoptViewModelFactory

    var binding by remember {
        mutableStateOf<ActivityOnboardingBinding?>(null)
    }

    val fragmentActivity = remember(context) {
        context.findActivity<FragmentActivity>()
    }

    val args = StartArgs(
        currentGeneration = 0,
        userStatus = userStatus.name
    )

    val onboardingUiState by viewModel.onboardingPokeUserListUiState.collectAsStateWithLifecycle()
    val checkNewInPokeState by viewModel.checkNewInPokeOnboardingState.collectAsStateWithLifecycle()
    var showErrorDialog by remember { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        tracker.track(
            type = EventType.VIEW,
            name = "poke_onboarding",
            properties = mapOf("view_type" to args.userStatus)
        )
        onPauseOrDispose {}
    }

    LaunchedEffect(checkNewInPokeState) {
        if (checkNewInPokeState == true && fragmentActivity != null) {
            showOnboardingBottomSheet(fragmentActivity, viewModel)
        }
    }

    LaunchedEffect(onboardingUiState) {
        if (onboardingUiState is UiState.ApiError || onboardingUiState is UiState.Failure) {
            showErrorDialog = true
        }
    }

    LaunchedEffect(onboardingUiState, binding) {
        val currentBinding = binding ?: return@LaunchedEffect

        when (val state = onboardingUiState) {
            is UiState.Success -> {
                val profiles = state.data

                if (fragmentActivity != null && currentBinding.viewpager.adapter == null) {
                    currentBinding.viewpager.adapter = OnboardingViewPagerAdapter(
                        fragmentActivity = fragmentActivity,
                        viewModelFactory = metroFactory,
                        tracker = tracker,
                        profiles = profiles,
                        args = args,
                    )
                    currentBinding.dotsIndicator.attachTo(currentBinding.viewpager)
                }

                currentBinding.layoutLottie.visibility = View.GONE
                currentBinding.viewpager.visibility = View.VISIBLE
                currentBinding.dotsIndicator.visibility = View.VISIBLE
            }
            is UiState.Loading -> {}
            else -> {}
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Text(
            text = "콕 찌르기",
            style = SoptTheme.typography.heading16B,
            color = colorResource(id = org.sopt.official.designsystem.R.color.mds_gray_30),
            modifier = Modifier
                .padding(16.dp)
        )

        Text(
            text = "아는 사람을 콕 찔러서 친구를 맺어보세요",
            modifier = Modifier
                .padding(horizontal = 16.dp),
            style = SoptTheme.typography.heading16B,
            color = colorResource(id = org.sopt.official.designsystem.R.color.mds_gray_30),
        )

        AndroidViewBinding(
            factory = ActivityOnboardingBinding::inflate,
        ) {
            binding = this
            if (viewpager.adapter == null) {
                animationViewLottie.addOnAnimationEndListener {
                    layoutLottie.visibility = View.GONE
                    navigateToPokeMain()
                }

                layoutLottie.setOnClickListener {}
            }
        }
    }

    if (showErrorDialog) {
        PokeErrorDialog(
            onCheckClick = {
                showErrorDialog = false
                navigateUp()
            }
        )
    }
}

private fun showOnboardingBottomSheet(
    activity: FragmentActivity,
    viewModel: OnboardingViewModel
) {
    val tag = "OnboardingBottomSheet"
    if (activity.supportFragmentManager.findFragmentByTag(tag) != null) return

    val sheet = activity.supportFragmentManager.fragmentFactory.instantiate(
        OnboardingBottomSheetFragment::class.java.classLoader!!,
        OnboardingBottomSheetFragment::class.java.name
    ) as OnboardingBottomSheetFragment

    sheet.onDismissEvent = { viewModel.updateNewInPokeOnboarding() }

    sheet.show(activity.supportFragmentManager, tag)
}
