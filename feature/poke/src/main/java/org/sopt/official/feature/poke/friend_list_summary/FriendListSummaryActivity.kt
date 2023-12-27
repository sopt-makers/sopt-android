package org.sopt.official.feature.poke.friend_list_summary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.common.util.viewBinding
import org.sopt.official.common.view.toast
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.poke_user_recycler_view.ItemDecorationDivider
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListAdapter
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListClickListener
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListItemViewType
import org.sopt.official.feature.poke.databinding.ActivityFriendListSummaryBinding
import org.sopt.official.feature.poke.friend_list_detail.FriendListDetailBottomSheetFragment
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment

@AndroidEntryPoint
class FriendListSummaryActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityFriendListSummaryBinding::inflate)
    private val viewModel by viewModels<FriendListSummaryViewModel>()

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    private val newFriendListAdapter
        get() = binding.includeFriendListBlockNewFriend.recyclerView.adapter as PokeUserListAdapter?
    private val bestFriendListAdapter
        get() = binding.includeFriendListBlockBestFriend.recyclerView.adapter as PokeUserListAdapter?
    private val soulmateListAdapter
        get() = binding.includeFriendListBlockSoulmate.recyclerView.adapter as PokeUserListAdapter?

    private val recyclerViewItemDecorationDivider
        get() = ItemDecorationDivider(
            color = resources.getColor(org.sopt.official.designsystem.R.color.mds_gray_800),
            height = 1 * resources.displayMetrics.density
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppBar()
        initFriendListBlock()
        launchFriendListSummaryUiStateFlow()
        launchPokeUserUiStateFlow()
    }

    private fun initAppBar() {
        binding.includeAppBar.apply {
            toolbar.setOnClickListener { onBackPressed() }
            textViewTitle.text = getString(R.string.my_friend_title)
        }
    }

    private fun initFriendListBlock() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getFriendListSummary()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.includeFriendListBlockNewFriend.apply {
            imageButtonMore.setOnClickListener { showFriendListDetailBottomSheet(PokeFriendType.NEW) }
            textViewFriendListTitle.text = getString(R.string.friend_relation_name_new_friend)
            textViewFriendListDescription.text = getString(R.string.friend_relation_description_new_friend)
            textViewListCount.text = getString(R.string.friend_list_count, 0)

            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter = PokeUserListAdapter(
                pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                clickListener = pokeUserListClickLister,
            )
        }

        binding.includeFriendListBlockBestFriend.apply {
            imageButtonMore.setOnClickListener { showFriendListDetailBottomSheet(PokeFriendType.BEST_FRIEND) }
            textViewFriendListTitle.text = getString(R.string.friend_relation_name_best_friend)
            textViewFriendListDescription.text = getString(R.string.friend_relation_description_best_friend)
            textViewListCount.text = getString(R.string.friend_list_count, 0)

            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter = PokeUserListAdapter(
                pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                clickListener = pokeUserListClickLister,
            )
        }

        binding.includeFriendListBlockSoulmate.apply {
            imageButtonMore.setOnClickListener { showFriendListDetailBottomSheet(PokeFriendType.SOULMATE) }
            textViewFriendListTitle.text = getString(R.string.friend_relation_name_soulmate)
            textViewFriendListDescription.text = getString(R.string.friend_relation_description_soulmate)
            textViewListCount.text = getString(R.string.friend_list_count, 0)

            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter = PokeUserListAdapter(
                pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                clickListener = pokeUserListClickLister,
            )
        }
    }

    private fun showFriendListDetailBottomSheet(pokeFriendType: PokeFriendType) {
        FriendListDetailBottomSheetFragment.Builder()
            .setPokeFriendType(pokeFriendType)
            .create()
            .let { it.show(supportFragmentManager, it.tag) }
    }

    private fun launchFriendListSummaryUiStateFlow() {
        viewModel.friendListSummaryUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<FriendListSummary> -> updateRecyclerView(it.data)
                    is UiState.ApiError -> it.responseMessage
                    is UiState.Failure -> it.throwable.message
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun updateRecyclerView(data: FriendListSummary) {
        binding.includeFriendListBlockNewFriend.apply {
            when (data.newFriend.isEmpty()) {
                true -> {
                    recyclerView.setVisible(false)
                    includeFriendListEmptyView.root.setVisible(true)
                }

                false -> {
                    recyclerView.setVisible(true)
                    includeFriendListEmptyView.root.setVisible(false)
                    textViewListCount.text = getString(R.string.friend_list_count, data.newFriendSize)
                    newFriendListAdapter?.submitList(data.newFriend)
                }
            }
        }

        binding.includeFriendListBlockBestFriend.apply {
            when (data.bestFriend.isEmpty()) {
                true -> {
                    recyclerView.setVisible(false)
                    includeFriendListEmptyView.root.setVisible(true)
                }

                false -> {
                    recyclerView.setVisible(true)
                    includeFriendListEmptyView.root.setVisible(false)
                    recyclerView
                    textViewListCount.text = getString(R.string.friend_list_count, data.bestFriendSize)
                    bestFriendListAdapter?.submitList(data.newFriend)
                }
            }
        }

        binding.includeFriendListBlockSoulmate.apply {
            when (data.soulmate.isEmpty()) {
                true -> {
                    recyclerView.setVisible(false)
                    includeFriendListEmptyView.root.setVisible(true)
                }

                false -> {
                    recyclerView.setVisible(true)
                    includeFriendListEmptyView.root.setVisible(false)
                    textViewListCount.text = getString(R.string.friend_list_count, data.soulmateSize)
                    soulmateListAdapter?.submitList(data.soulmate)
                }
            }
        }
    }

    private val pokeUserListClickLister = object : PokeUserListClickListener {
        override fun onClickProfileImage(playgroundId: Int) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, playgroundId))))
        }

        override fun onClickPokeButton(userId: Int) {
            if (messageListBottomSheet?.isAdded == true) return
            if (messageListBottomSheet == null) {
                messageListBottomSheet = MessageListBottomSheetFragment.Builder()
                    .setMessageListType(PokeMessageType.POKE_SOMEONE)
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
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        finish()
                    }

                    is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                    is UiState.Failure -> it.throwable.message?.let { toast(it) }
                }
            }
            .launchIn(lifecycleScope)
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, FriendListSummaryActivity::class.java)
    }
}