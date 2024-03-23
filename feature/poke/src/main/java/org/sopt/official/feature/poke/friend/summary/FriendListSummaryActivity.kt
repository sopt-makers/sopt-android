/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.friend.summary

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
import org.sopt.official.analytics.AmplitudeTracker
import org.sopt.official.analytics.EventType
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.util.colorOf
import org.sopt.official.common.util.dp
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.common.util.viewBinding
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityFriendListSummaryBinding
import org.sopt.official.feature.poke.friend.detail.FriendListDetailBottomSheetFragment
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.ItemDecorationDivider
import org.sopt.official.feature.poke.user.PokeUserListAdapter
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.user.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.showPokeToast
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class FriendListSummaryActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityFriendListSummaryBinding::inflate)
    private val viewModel by viewModels<FriendListSummaryViewModel>()

    private val args by serializableExtra(StartArgs(""))
    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    @Inject
    lateinit var tracker: AmplitudeTracker

    private val newFriendListAdapter
        get() = binding.includeFriendListBlockNewFriend.recyclerView.adapter as PokeUserListAdapter?
    private val bestFriendListAdapter
        get() = binding.includeFriendListBlockBestFriend.recyclerView.adapter as PokeUserListAdapter?
    private val soulmateListAdapter
        get() = binding.includeFriendListBlockSoulmate.recyclerView.adapter as PokeUserListAdapter?

    private val recyclerViewItemDecorationDivider
        get() =
            ItemDecorationDivider(
                color = colorOf(org.sopt.official.designsystem.R.color.mds_gray_800),
                height = 1.dp,
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppBar()
        initFriendListBlock()
        launchFriendListSummaryUiStateFlow()
        launchPokeUserUiStateFlow()
    }

    override fun onResume() {
        super.onResume()
        tracker.track(type = EventType.VIEW, name = "poke_friend", properties = mapOf("view_type" to args?.userStatus))
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
            imageButton.setBackgroundResource(R.drawable.icon_chevron)
            imageButton.setOnClickListener { showFriendListDetailBottomSheet(PokeFriendType.NEW) }
            textViewFriendListTitle.text = getString(R.string.friend_relation_name_new_friend)
            textViewFriendListDescription.text = getString(R.string.friend_relation_description_new_friend)
            textViewListCount.text = getString(R.string.friend_list_count, 0)

            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter =
                PokeUserListAdapter(
                    pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                    clickListener = pokeUserListClickLister,
                )
        }

        binding.includeFriendListBlockBestFriend.apply {
            imageButton.setBackgroundResource(R.drawable.icon_chevron)
            imageButton.setOnClickListener { showFriendListDetailBottomSheet(PokeFriendType.BEST_FRIEND) }
            textViewFriendListTitle.text = getString(R.string.friend_relation_name_best_friend)
            textViewFriendListDescription.text = getString(R.string.friend_relation_description_best_friend)
            textViewListCount.text = getString(R.string.friend_list_count, 0)

            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter =
                PokeUserListAdapter(
                    pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                    clickListener = pokeUserListClickLister,
                )
        }

        binding.includeFriendListBlockSoulmate.apply {
            imageButton.setBackgroundResource(R.drawable.icon_chevron)
            imageButton.setOnClickListener { showFriendListDetailBottomSheet(PokeFriendType.SOULMATE) }
            textViewFriendListTitle.text = getString(R.string.friend_relation_name_soulmate)
            textViewFriendListDescription.text = getString(R.string.friend_relation_description_soulmate)
            textViewListCount.text = getString(R.string.friend_list_count, 0)

            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter =
                PokeUserListAdapter(
                    pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                    clickListener = pokeUserListClickLister,
                )
        }
    }

    private fun showFriendListDetailBottomSheet(pokeFriendType: PokeFriendType) {
        FriendListDetailBottomSheetFragment.Builder()
            .setUserStatus(args?.userStatus ?: UserStatus.UNAUTHENTICATED.name)
            .setPokeFriendType(pokeFriendType)
            .create()
            .let { it.show(supportFragmentManager, it.tag) }
    }

    private fun launchFriendListSummaryUiStateFlow() {
        viewModel.friendListSummaryUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<FriendListSummary> -> updateRecyclerView(it.data)
                    is UiState.ApiError -> showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
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
                    bestFriendListAdapter?.submitList(data.bestFriend)
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

    private val pokeUserListClickLister =
        object : PokeUserListClickListener {
            override fun onClickProfileImage(playgroundId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties =
                    mapOf(
                        "view_type" to args?.userStatus,
                        "click_view_type" to "friend",
                        "view_profile" to playgroundId,
                    ),
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
                        "click_view_type" to "friend",
                        "view_profile" to user.playgroundId,
                    ),
                )
                messageListBottomSheet =
                    MessageListBottomSheetFragment.Builder()
                        .setMessageListType(PokeMessageType.POKE_FRIEND)
                        .onClickMessageListItem { message -> viewModel.pokeUser(user.userId, message) }
                        .create()

                messageListBottomSheet?.let {
                    it.show(supportFragmentManager, it.tag)
                }
            }
        }

    private fun launchPokeUserUiStateFlow() {
        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        viewModel.getFriendListSummary()
                        showPokeToast(getString(R.string.toast_poke_user_success))
                    }

                    is UiState.ApiError -> {
                        messageListBottomSheet?.dismiss()
                        showPokeToast(getString(R.string.poke_user_alert_exceeded))
                    }

                    is UiState.Failure -> {
                        messageListBottomSheet?.dismiss()
                        showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    data class StartArgs(
        val userStatus: String,
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, FriendListSummaryActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}
