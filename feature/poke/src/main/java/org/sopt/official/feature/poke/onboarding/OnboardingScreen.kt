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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.context.findActivity
import org.sopt.official.designsystem.SoptTheme
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
            if (viewpager.adapter == null) {
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
