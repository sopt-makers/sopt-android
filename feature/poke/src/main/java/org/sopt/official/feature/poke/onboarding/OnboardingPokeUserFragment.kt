package org.sopt.official.feature.poke.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.analytics.AmplitudeTracker
import org.sopt.official.analytics.EventType
import org.sopt.official.common.util.serializableArgs
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.FragmentOnboardingFriendsBinding
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.onboarding.model.PokeOnboardingUiState
import org.sopt.official.feature.poke.onboarding.model.toSerializable
import org.sopt.official.feature.poke.user.PokeUserListAdapter
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.user.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.showPokeToast
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingPokeUserFragment : Fragment() {
    private var _binding: FragmentOnboardingFriendsBinding? = null
    private val binding
        get() = requireNotNull(_binding) { "ERROR" }

    private val viewModel: OnboardingPokeUserViewModel by viewModels()

    private val args by serializableArgs<OnboardingActivity.StartArgs>()
    // private val argss by serializableExtraFragment(OnboardingActivity.StartArgs(0, ""))

    @Inject
    lateinit var tracker: AmplitudeTracker

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null
    private val pokeUserListAdapter
        get() = binding.recyclerView.adapter as PokeUserListAdapter?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        getPokeUserListFromArguments()
        launchOnboardingPokeUserListUiStateFlow()
        launchPokeUserUiStateFlow()
    }

    override fun onResume() {
        super.onResume()
        tracker.track(
            type = EventType.VIEW,
            name = "poke_onboarding_fragment",
            properties = mapOf("view_type" to args?.userStatus)
        )
    }

    private fun initView() {
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getOnboardingPokeUserList()
            }
            recyclerView.adapter =
                PokeUserListAdapter(
                    pokeUserListItemViewType = PokeUserListItemViewType.LARGE,
                    clickListener = pokeUserListClickLister,
                )
        }
    }

    private fun getPokeUserListFromArguments() {
        val pokeUser =
            BundleCompat.getSerializable(arguments ?: Bundle(), ARG_PROFILES, PokeOnboardingUiState::class.java)
        updateRecyclerView(pokeUser?.randomTitle.orEmpty(), pokeUser?.userInfoList.orEmpty().map { it.toEntity() })
    }

    private fun launchOnboardingPokeUserListUiStateFlow() {
        viewModel.onboardingPokeUserListUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeRandomUserList.PokeRandomUsers> -> updateRecyclerView(it.data.randomType, it.data.userInfoList)
                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }.launchIn(lifecycleScope)
    }

    private fun launchPokeUserUiStateFlow() {
        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        when (it.isFirstMeet && !it.data.isFirstMeet) {
                            true -> (activity as OnboardingActivity).playLottieAnimation(it.data.name)
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
            }.launchIn(lifecycleScope)
    }

    private fun updateRecyclerView(textViewContent: String, data: List<PokeUser>) {
        viewModel.setRandomType(textViewContent)
        binding.textViewContent.text = textViewContent
        pokeUserListAdapter?.submitList(data)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private val pokeUserListClickLister =
        object : PokeUserListClickListener {
            override fun onClickProfileImage(playgroundId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties = mapOf("view_type" to args?.userStatus, "click_view_type" to "onboarding", "view_profile" to playgroundId),
                )
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, playgroundId))))
            }

            override fun onClickPokeButton(user: PokeUser) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_icon",
                    properties =
                    mapOf(
                        "view_type" to args?.userStatus,
                        "click_view_type" to "onboarding",
                        "view_profile" to user.playgroundId,
                    ),
                )

                messageListBottomSheet =
                    MessageListBottomSheetFragment.Builder()
                        .setMessageListType(PokeMessageType.POKE_SOMEONE)
                        .onClickMessageListItem { message, isAnonymous ->
                            viewModel.pokeUser(
                                userId = user.userId,
                                isAnonymous = isAnonymous,
                                message = message
                            )
                        }
                        .create()

                messageListBottomSheet?.let {
                    it.show(requireActivity().supportFragmentManager, it.tag)
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PROFILES = "profiles"
        private const val ARG_ARGS = "args"

        @JvmStatic
        fun newInstance(pokeUsers: PokeRandomUserList.PokeRandomUsers, args: OnboardingActivity.StartArgs?) =
            OnboardingPokeUserFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PROFILES, pokeUsers.toSerializable())
                    putSerializable(ARG_ARGS, args)
                }
            }
    }
}
