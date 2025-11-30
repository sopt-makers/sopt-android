package org.sopt.official.feature.poke.onboarding

import android.view.View
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.context.findActivity
import org.sopt.official.feature.poke.R
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
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tracker = LocalTracker.current
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

    AndroidViewBinding(
        factory = ActivityOnboardingBinding::inflate,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        if (viewpager.adapter == null) {
            includeAppBar.textViewTitle.text = root.context.getString(R.string.poke_title)

            animationViewLottie.addOnAnimationEndListener {
                layoutLottie.visibility = View.GONE
                navigateToPokeMain()
            }

            layoutLottie.setOnClickListener {}
        }

        when (val state = onboardingUiState) {
            is UiState.Success -> {
                val profiles = state.data

                if (fragmentActivity != null && viewpager.adapter == null) {
                    viewpager.adapter = OnboardingViewPagerAdapter(
                        fragmentActivity = fragmentActivity,
                        profiles = profiles,
                        args = args,
                    )
                    dotsIndicator.attachTo(viewpager)
                }

                layoutLottie.visibility = View.GONE
                viewpager.visibility = View.VISIBLE
                dotsIndicator.visibility = View.VISIBLE
            }
            is UiState.Loading -> {

            }
            else -> {

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

    val sheet = OnboardingBottomSheetFragment.Builder()
        .setOnDismissEvent { viewModel.updateNewInPokeOnboarding() }
        .create()

    sheet.show(activity.supportFragmentManager, tag)
}
