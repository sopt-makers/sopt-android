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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.common.util.serializableArgs
import org.sopt.official.common.di.SoptViewModelFactory
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.FragmentOnboardingFriendsBinding
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.onboarding.model.PokeOnboardingUiState
import org.sopt.official.feature.poke.onboarding.model.StartArgs
import org.sopt.official.feature.poke.onboarding.model.toSerializable
import org.sopt.official.feature.poke.user.PokeUserListAdapter
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.user.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.showPokeToast

class OnboardingPokeUserFragment(
    private val viewModelFactory: SoptViewModelFactory,
    private val tracker: Tracker
) : Fragment() {
    private var _binding: FragmentOnboardingFriendsBinding? = null
    private val binding
        get() = requireNotNull(_binding) { "ERROR" }

    private lateinit var viewModel: OnboardingPokeUserViewModel

    private val args by serializableArgs<OnboardingActivity.StartArgs>()

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null
    private val pokeUserListAdapter
        get() = binding.recyclerView.adapter as PokeUserListAdapter?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingFriendsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[OnboardingPokeUserViewModel::class.java]
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
        pokeUser?.let {
            updateRecyclerView(it.randomType, it.randomTitle, it.userInfoList.map { pokeUser -> pokeUser.toEntity() })
        }
    }

    private fun launchOnboardingPokeUserListUiStateFlow() {
        viewModel.onboardingPokeUserListUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success<PokeRandomUserList.PokeRandomUsers> -> updateRecyclerView(
                        it.data.randomType,
                        it.data.randomTitle,
                        it.data.userInfoList
                    )

                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }.launchIn(lifecycleScope)
    }

    private fun launchPokeUserUiStateFlow() {
        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
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

    private fun updateRecyclerView(randomType: String, randomTitle: String, data: List<PokeUser>) {
        viewModel.setRandomType(randomType)
        binding.textViewContent.text = randomTitle
        pokeUserListAdapter?.submitList(data)
        binding.swipeRefreshLayout.isRefreshing = false
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

                val bottomSheet = requireActivity().supportFragmentManager.fragmentFactory.instantiate(
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
                        message = message
                    )
                }

                bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
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
        fun newInstance(
            viewModelFactory: SoptViewModelFactory,
            tracker: Tracker,
            pokeUsers: PokeRandomUserList.PokeRandomUsers,
            args: StartArgs?
        ) = OnboardingPokeUserFragment(viewModelFactory, tracker).apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PROFILES, pokeUsers.toSerializable())
                putSerializable(ARG_ARGS, args)
            }
        }
    }
}