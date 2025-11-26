package org.sopt.official.feature.poke.onboarding

import android.view.LayoutInflater
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.context.findActivity
import org.sopt.official.domain.poke.entity.PokeRandomUserList
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

    var showErrorDialog by remember { mutableStateOf(false) }

    val fragmentActivity = remember(context) {
        context.findActivity<FragmentActivity>()
    }

    val binding = remember {
        ActivityOnboardingBinding.inflate(LayoutInflater.from(context))
    }

    val args = StartArgs(
        currentGeneration = 0,
        userStatus = userStatus.name
    )

    val onboardingUiStateFlow = viewModel.onboardingPokeUserListUiState
    val checkNewInPokeState by viewModel.checkNewInPokeOnboardingState.collectAsStateWithLifecycle()
    var currentUiState by remember { mutableStateOf<UiState<PokeRandomUserList>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        with(binding) {
            includeAppBar.textViewTitle.text = binding.root.context.getString(R.string.poke_title)

            animationViewLottie.addOnAnimationEndListener {
                layoutLottie.visibility = View.GONE
                navigateToPokeMain()
            }

            layoutLottie.setOnClickListener {
                // do nothing
            }
        }
    }

    LaunchedEffect(checkNewInPokeState) {
        if (checkNewInPokeState == true && fragmentActivity != null) {
            showOnboardingBottomSheet(fragmentActivity, viewModel)
        }
    }

    LaunchedEffect(Unit) {
        onboardingUiStateFlow.collect { state ->
            currentUiState = state

            if (state is UiState.Success) {
                val profiles = state.data
                fragmentActivity?.let { activity ->
                    if (binding.viewpager.adapter == null) {
                        binding.viewpager.adapter = OnboardingViewPagerAdapter(
                            fragmentActivity = activity,
                            profiles = profiles,
                            args = args,
                        )
                        binding.dotsIndicator.attachTo(binding.viewpager)
                    }
                }
            } else if (state is UiState.ApiError || state is UiState.Failure) {
                showErrorDialog = true
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        tracker.track(
            type = EventType.VIEW,
            name = "poke_onboarding",
            properties = mapOf("view_type" to args.userStatus)
        )
        onPauseOrDispose {}
    }

    AndroidView(
        factory = {
            binding.root
        },
        update = {
            when (currentUiState) {
                is UiState.Success -> {
                    binding.layoutLottie.visibility = View.GONE
                    binding.viewpager.visibility = View.VISIBLE
                    binding.dotsIndicator.visibility = View.VISIBLE
                }
                is UiState.Loading -> {

                }
                else -> {

                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    )

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
