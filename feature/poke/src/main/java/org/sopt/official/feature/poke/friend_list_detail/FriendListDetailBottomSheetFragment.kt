package org.sopt.official.feature.poke.friend_list_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.common.view.toast
import org.sopt.official.domain.poke.entity.FriendListDetail
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.common_recycler_view.ItemDecorationDivider
import org.sopt.official.feature.poke.common_recycler_view.PokeUserListAdapter
import org.sopt.official.feature.poke.common_recycler_view.PokeUserListClickListener
import org.sopt.official.feature.poke.common_recycler_view.PokeUserListItemViewType
import org.sopt.official.feature.poke.databinding.FragmentFriendListDetailBottomSheetBinding
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment


@AndroidEntryPoint
class FriendListDetailBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFriendListDetailBottomSheetBinding? = null
    private val binding: FragmentFriendListDetailBottomSheetBinding get() = requireNotNull(_binding)
    private lateinit var viewModel: FriendListDetailViewModel

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    private val pokeFriendListAdapter
        get() = binding.includeFriendListBlock.recyclerView.adapter as PokeUserListAdapter?

    private val recyclerViewItemDecorationDivider
        get() = ItemDecorationDivider(
            color = resources.getColor(org.sopt.official.designsystem.R.color.mds_gray_800),
            height = 1 * resources.displayMetrics.density
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

            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter = PokeUserListAdapter(
                pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                clickListener = pokeUserListClickLister,
            )
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
                it.show(parentFragmentManager, it.tag)
            }
        }
    }

    private fun launchPokeMessageListUiStateFlow() {
        viewModel.friendListDetailUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<FriendListDetail> -> updateRecyclerView(it.data.friendList)
                    is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                    is UiState.Failure -> it.throwable.message?.let { toast(it) }
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
                    pokeFriendListAdapter?.submitList(data)
                }
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
                        dismiss()
                    }

                    is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                    is UiState.Failure -> it.throwable.message?.let { toast(it) }
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