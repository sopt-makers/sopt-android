package org.sopt.official.feature.poke.friend_list_summary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.viewBinding
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityFriendListSummaryBinding

class FriendListSummaryActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityFriendListSummaryBinding::inflate)
    private val viewModel by viewModels<FriendListSummaryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        launchFriendListSummaryUiStateFlow()
    }

    private fun launchFriendListSummaryUiStateFlow() {
        viewModel.friendListSummaryUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<FriendListSummary> -> it.data.toString()
                    is UiState.ApiError -> it.responseMessage
                    is UiState.Failure -> it.throwable.message
                }
            }
            .launchIn(lifecycleScope)
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, FriendListSummaryActivity::class.java)
    }
}