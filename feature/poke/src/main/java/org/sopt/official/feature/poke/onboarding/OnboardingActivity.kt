package org.sopt.official.feature.poke.onboarding

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.viewBinding
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityOnboardingBinding
import org.sopt.official.feature.poke.main.PokeMainActivity
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListAdapter
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListClickListener
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.showAlertToast
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

        initAppBar()
        initView()
        showOnboardingBottomSheet()

        launchOnboardingPokeUserListUiStateFlow()
        launchPokeUserUiStateFlow()
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
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, playgroundId))))
        }

        override fun onClickPokeButton(user: PokeUser) {
            if (messageListBottomSheet?.isAdded == true) return
            if (messageListBottomSheet == null) {
                messageListBottomSheet = MessageListBottomSheetFragment.Builder()
                    .setMessageListType(PokeMessageType.POKE_SOMEONE)
                    .onClickMessageListItem { message -> viewModel.pokeUser(user.userId, message) }
                    .create()
            }

            messageListBottomSheet?.let {
                it.show(supportFragmentManager, it.tag)
            }
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
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<List<PokeUser>> -> initRecyclerView(it.data)
                    is UiState.ApiError -> showAlertToast(getString(R.string.poke_alert_error))
                    is UiState.Failure -> showAlertToast(it.throwable.message ?: getString(R.string.poke_alert_error))
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initRecyclerView(data: List<PokeUser>) {
        binding.swipeRefreshLayout.isRefreshing = false
        pokeUserListAdapter?.submitList(data)
    }

    private fun launchPokeUserUiStateFlow() {
        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        startActivity(Intent(this, PokeMainActivity::class.java))
                        finish()
                    }

                    is UiState.ApiError -> {
                        messageListBottomSheet?.dismiss()
                        showAlertToast(getString(R.string.poke_alert_error))
                    }

                    is UiState.Failure -> {
                        messageListBottomSheet?.dismiss()
                        showAlertToast(it.throwable.message ?: getString(R.string.poke_alert_error))
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    data class StartArgs(
        val currentGeneration: Int
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) =
            Intent(context, OnboardingActivity::class.java).apply {
                putExtra("args", args)
            }
    }
}