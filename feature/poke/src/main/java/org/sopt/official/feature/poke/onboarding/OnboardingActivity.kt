package org.sopt.official.feature.poke.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.viewBinding
import org.sopt.official.common.view.toast
import org.sopt.official.domain.poke.entity.CheckNewInPoke
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityOnboardingBinding
import org.sopt.official.feature.poke.friend_list_summary.FriendListSummaryActivity
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.recycler_view.PokeUserListAdapter
import org.sopt.official.feature.poke.recycler_view.PokeUserListClickListener
import org.sopt.official.feature.poke.recycler_view.PokeUserListItemViewType
import java.io.Serializable

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityOnboardingBinding::inflate)
    private val viewModel by viewModels<OnboardingViewModel>()

    private val args by serializableExtra(StartArgs(0))

    private var onboardingBottomSheet: OnboardingBottomSheetFragment? = null
    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    private val pokeUserListAdapter
        get() = binding.recyclerView.adapter as PokeUserListAdapter?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        launchCheckNewInPokeUiStateFlow()
        launchOnboardingPokeUserListUiStateFlow()
        launchPokeUserUiStateFlow()
    }

    private fun initView() {
        binding.textViewContent.text = getString(R.string.onboarding_content, args?.recentGeneration)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getOnboardingPokeUserList()
        }
    }

    private fun launchCheckNewInPokeUiStateFlow() {
        viewModel.checkNewInPokeUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<CheckNewInPoke> -> handleNewInPoke(it.data.isNew)
                    is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                    is UiState.Failure -> it.throwable.message?.let { toast(it) }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun handleNewInPoke(isNewInPoke: Boolean) {
        when (isNewInPoke) {
            true -> showOnboardingBottomSheet()
            false -> FriendListSummaryActivity.getIntent(this)
        }
    }

    private fun showOnboardingBottomSheet() {
        if (onboardingBottomSheet?.isAdded == true) return
        if (onboardingBottomSheet == null) {
            onboardingBottomSheet = OnboardingBottomSheetFragment()
        }
        onboardingBottomSheet?.let {
            it.show(supportFragmentManager, it.tag)
        }
    }

    private fun launchOnboardingPokeUserListUiStateFlow() {
        viewModel.onboardingPokeUserListUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<List<PokeUser>> -> initRecyclerView(it.data)
                    is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                    is UiState.Failure -> it.throwable.message?.let { toast(it) }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initRecyclerView(data: List<PokeUser>) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.recyclerView.apply {
            adapter = PokeUserListAdapter(
                pokeUserListItemViewType = PokeUserListItemViewType.LARGE,
                clickListener = pokeUserListClickLister,
            )
            pokeUserListAdapter?.submitList(data)
        }
    }

    private val pokeUserListClickLister = object : PokeUserListClickListener {
        override fun onClickProfileImage() {
            // TODO("Navigate to playground")
        }

        override fun onClickPokeButton(userId: Int) {
            if (messageListBottomSheet?.isAdded == true) return
            if (messageListBottomSheet == null) {
                messageListBottomSheet = MessageListBottomSheetFragment.Builder()
                    .onClickMessageListItem { message -> viewModel.pokeUser(userId, message) }
                    .create()
            }

            messageListBottomSheet?.let {
                it.show(supportFragmentManager, it.tag)
            }
        }
    }

    private fun launchPokeUserUiStateFlow() {
        viewModel.pokeUserUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> { messageListBottomSheet?.dismiss() }
                    is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                    is UiState.Failure -> it.throwable.message?.let { toast(it) }
                }
            }
            .launchIn(lifecycleScope)
    }

    data class StartArgs(
        val recentGeneration: Int
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) =
            Intent(context, OnboardingActivity::class.java).apply {
                putExtra("args", args)
            }
    }
}