/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.friend_list_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.dp
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.domain.poke.entity.FriendListDetail
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.FragmentFriendListDetailBottomSheetBinding
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.poke_user_recycler_view.ItemDecorationDivider
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListAdapter
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListClickListener
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.showPokeToast


@AndroidEntryPoint
class FriendListDetailBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFriendListDetailBottomSheetBinding? = null
    private val binding: FragmentFriendListDetailBottomSheetBinding get() = requireNotNull(_binding)
    private lateinit var viewModel: FriendListDetailViewModel

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    private val pokeFriendListAdapter
        get() = binding.includeFriendListBlock.recyclerView.adapter as PokeUserListAdapter?
    private val pokeFriendListLayoutManager
        get() = binding.includeFriendListBlock.recyclerView.layoutManager as LinearLayoutManager

    private val recyclerViewItemDecorationDivider
        get() = ItemDecorationDivider(
            color = resources.getColor(org.sopt.official.designsystem.R.color.mds_gray_800),
            height = 1.dp
        )

    var pokeFriendType: PokeFriendType? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendListDetailBottomSheetBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(FriendListDetailViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokeFriendType?.let { viewModel.getFriendListDetail(it) }

        initRecyclerView()
        launchPokeMessageListUiStateFlow()
        launchPokeUserUiStateFlow()
    }

    private fun initRecyclerView() {
        binding.includeFriendListBlock.apply {
            pokeFriendType?.let {
                when (it) {
                    PokeFriendType.NEW -> {
                        textViewFriendListTitle.text = getString(R.string.friend_relation_name_new_friend)
                        textViewFriendListDescription.text = getString(R.string.friend_relation_description_new_friend)
                        textViewListCount.text = getString(R.string.friend_list_count, 0)
                    }

                    PokeFriendType.BEST_FRIEND -> {
                        textViewFriendListTitle.text = getString(R.string.friend_relation_name_best_friend)
                        textViewFriendListDescription.text = getString(R.string.friend_relation_description_best_friend)
                        textViewListCount.text = getString(R.string.friend_list_count, 0)
                    }

                    PokeFriendType.SOULMATE -> {
                        textViewFriendListTitle.text = getString(R.string.friend_relation_name_soulmate)
                        textViewFriendListDescription.text = getString(R.string.friend_relation_description_soulmate)
                        textViewListCount.text = getString(R.string.friend_list_count, 0)
                    }
                }
            }

            imageButton.setBackgroundResource(R.drawable.icon_close)
            imageButton.setOnClickListener { dismiss() }
            recyclerView.addOnScrollListener(scrollListener)
            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter = PokeUserListAdapter(
                pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                clickListener = pokeUserListClickLister,
            )
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisibleItemPosition = pokeFriendListLayoutManager.findLastVisibleItemPosition()
            val totalItemCount = pokeFriendListLayoutManager.itemCount
            if (lastVisibleItemPosition == totalItemCount - 1 && totalItemCount % 20 == 0) {
                pokeFriendType?.let { viewModel.getFriendListDetail(it) }
            }
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
                    .setMessageListType(PokeMessageType.POKE_FRIEND)
                    .onClickMessageListItem { message -> viewModel.pokeUser(user.userId, message) }
                    .create()
            }

            messageListBottomSheet?.let {
                it.show(parentFragmentManager, it.tag)
            }
        }
    }

    private fun launchPokeMessageListUiStateFlow() {
        viewModel.friendListDetailUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<FriendListDetail> -> updateRecyclerView(it.data.friendList)
                    is UiState.ApiError -> activity?.showPokeToast(getString(R.string.toast_poke_error))
                    is UiState.Failure -> activity?.showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun updateRecyclerView(data: List<PokeUser>) {
        binding.includeFriendListBlock.apply {
            when (data.isEmpty()) {
                true -> {
                    recyclerView.setVisible(false)
                    includeFriendListEmptyView.root.setVisible(true)
                }

                false -> {
                    recyclerView.setVisible(true)
                    includeFriendListEmptyView.root.setVisible(false)
                    textViewListCount.text = getString(R.string.friend_list_count, data.size)
                    pokeFriendListAdapter?.updatePokeUserList(data)
                }
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
                        pokeFriendListAdapter?.updatePokeUserItemPokeState(it.data.userId)
                        activity?.showPokeToast(getString(R.string.toast_poke_user_success))
                    }

                    is UiState.ApiError -> {
                        messageListBottomSheet?.dismiss()
                        activity?.showPokeToast(getString(R.string.poke_user_alert_exceeded))
                    }

                    is UiState.Failure -> {
                        messageListBottomSheet?.dismiss()
                        activity?.showPokeToast(it.throwable.message ?: getString(R.string.toast_poke_error))
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    class Builder {
        private val bottomSheet = FriendListDetailBottomSheetFragment()
        fun create(): FriendListDetailBottomSheetFragment = bottomSheet

        fun setPokeFriendType(pokeFriendType: PokeFriendType): Builder {
            bottomSheet.pokeFriendType = pokeFriendType
            return this
        }
    }
}
