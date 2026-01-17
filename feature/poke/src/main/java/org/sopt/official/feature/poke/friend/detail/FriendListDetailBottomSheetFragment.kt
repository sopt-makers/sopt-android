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
package org.sopt.official.feature.poke.friend.detail

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
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.common.util.colorOf
import org.sopt.official.common.util.dp
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.common.util.viewBinding
import org.sopt.official.common.di.SoptViewModelFactory
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.FragmentFriendListDetailBottomSheetBinding
import org.sopt.official.feature.poke.main.PokeMainActivity
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.ItemDecorationDivider
import org.sopt.official.feature.poke.user.PokeUserListAdapter
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.user.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.feature.poke.util.setRelationStrokeColor
import org.sopt.official.feature.poke.util.showPokeToast

class FriendListDetailBottomSheetFragment(
    private val viewModelFactory: SoptViewModelFactory,
    private val tracker: Tracker
) : BottomSheetDialogFragment() {
    private val binding by viewBinding(FragmentFriendListDetailBottomSheetBinding::bind)
    private lateinit var viewModel: FriendListDetailViewModel

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    private val pokeFriendListAdapter
        get() = binding.includeFriendListBlock.recyclerView.adapter as PokeUserListAdapter?
    private val pokeFriendListLayoutManager
        get() = binding.includeFriendListBlock.recyclerView.layoutManager as LinearLayoutManager

    private val recyclerViewItemDecorationDivider
        get() =
            ItemDecorationDivider(
                color = colorOf(org.sopt.official.designsystem.R.color.mds_gray_800),
                height = 1.dp,
            )

    var userStatus: String? = null
    var pokeFriendType: PokeFriendType? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this, viewModelFactory)[FriendListDetailViewModel::class.java]
        return FragmentFriendListDetailBottomSheetBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userStatus = arguments?.getString("userStatus")
        pokeFriendType = arguments?.getSerializable("pokeFriendType") as? PokeFriendType

        pokeFriendType?.let {
            viewModel.getFriendListDetail(it)
            tracker.track(
                type = EventType.VIEW,
                name = "poke_friend_detail",
                properties =
                    mapOf(
                        "view_type" to userStatus,
                        "friend_type" to
                            when (it) {
                                PokeFriendType.NEW -> "newFriend"
                                PokeFriendType.BEST_FRIEND -> "bestFriend"
                                PokeFriendType.SOULMATE -> "soulmate"
                            },
                    ),
            )
        }

        initLottieListener()
        initRecyclerView()
        launchPokeMessageListUiStateFlow()
        launchPokeUserUiStateFlow()
    }

    private fun initLottieListener() {
        with(binding) {
            animationFriendViewLottie.addOnAnimationEndListener {
                if (viewModel.anonymousFriend.value != null) { // 천생연분 -> 정체 공개
                    lifecycleScope.launch {
                        // 로티
                        layoutAnonymousFriendLottie.visibility = View.GONE
                        layoutAnonymousFriendOpen.visibility = View.VISIBLE

                        val anonymousFriend = viewModel.anonymousFriend.value
                        anonymousFriend?.let {
                            tvAnonymousFreindName.text = getString(R.string.anonymous_user_identity, it.anonymousName)
                            tvAnonymousFreindInfo.text = getString(R.string.anonymous_user_info, it.generation, it.part, it.name)
                            imgAnonymousFriendOpen.load(it.profileImage.ifEmpty { R.drawable.ic_empty_profile }) {
                                transformations(CircleCropTransformation())
                            }

                            imgAnonymousFriendOpenOutline.setRelationStrokeColor(it.mutualRelationMessage)
                        }

                        delay(2000)
                        layoutAnonymousFriendOpen.visibility = View.GONE
                        viewModel.setAnonymousFriend(null)
                    }
                } else {
                    layoutAnonymousFriendLottie.visibility = View.GONE
                }
            }
        }
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

            imageButton.setOnClickListener { dismiss() }
            recyclerView.addOnScrollListener(scrollListener)
            recyclerView.addItemDecoration(recyclerViewItemDecorationDivider)
            recyclerView.adapter =
                PokeUserListAdapter(
                    pokeUserListItemViewType = PokeUserListItemViewType.SMALL,
                    clickListener = pokeUserListClickLister,
                )
        }
    }

    private fun initLottieView() {
        with(binding) {
            layoutAnonymousFriendOpen.visibility = View.GONE
            layoutAnonymousFriendLottie.visibility = View.GONE
        }
    }

    private val scrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = pokeFriendListLayoutManager.findLastVisibleItemPosition()
                val totalItemCount = pokeFriendListLayoutManager.itemCount
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    pokeFriendType?.let { viewModel.getFriendListDetail(it) }
                }
            }
        }

    private val pokeUserListClickLister =
        object : PokeUserListClickListener {
            override fun onClickProfileImage(userId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties =
                        mapOf(
                            "view_type" to userStatus,
                            "click_view_type" to "friend_detail",
                            "view_profile" to userId,
                        ),
                )
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, userId))))
            }

            override fun onClickPokeButton(user: PokeUser) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "poke_icon",
                    properties =
                        mapOf(
                            "view_type" to userStatus,
                            "click_view_type" to "friend_detail",
                            "view_profile" to user.userId,
                        ),
                )
                messageListBottomSheet =
                    MessageListBottomSheetFragment.Builder()
                        .setMessageListType(PokeMessageType.POKE_FRIEND)
                        .onClickMessageListItem { message, isAnonymous ->
                            viewModel.pokeUser(
                                userId = user.userId,
                                isAnonymous = isAnonymous,
                                message = message
                            )
                        }
                        .create()

                messageListBottomSheet?.let {
                    it.show(parentFragmentManager, it.tag)
                }
            }
        }

    private fun launchPokeMessageListUiStateFlow() {
        viewModel.friendListDetailUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success<List<PokeUser>> -> updateRecyclerView(it.data)
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
                    pokeFriendListAdapter?.submitList(data)
                }
            }
        }
    }

    private fun launchPokeUserUiStateFlow() {
        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        if (PokeMainActivity.isBestFriend(it.data.pokeNum, it.data.isAnonymous)) {
                            with(binding) {
                                layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                tvFreindLottie.text = getString(R.string.anonymous_to_friend, it.data.anonymousName, "단짝친구가")
                                tvFreindLottieHint.text =
                                    getString(R.string.anonymous_user_info_part, it.data.generation, it.data.part)
                                animationFriendViewLottie.apply {
                                    setAnimation(R.raw.friendtobestfriend)
                                }.playAnimation()
                            }
                        } else if (PokeMainActivity.isSoulMate(it.data.pokeNum, it.data.isAnonymous)) {
                            viewModel.setAnonymousFriend(it.data)
                            with(binding) {
                                layoutAnonymousFriendLottie.visibility = View.VISIBLE
                                tvFreindLottie.text = getString(R.string.anonymous_to_friend, it.data.anonymousName, "천생연분이")
                                animationFriendViewLottie.apply {
                                    setAnimation(R.raw.bestfriendtosoulmate)
                                }.playAnimation()
                            }
                        } else {
                            showPokeToast(getString(R.string.toast_poke_user_success))
                        }
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

    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = (dialog as? BottomSheetDialog)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheetDialog?.let {
            val layoutParams = it.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams = layoutParams

            val bottomSheetBehavior = BottomSheetBehavior.from(it)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isHideable = true
        }
    }

    override fun onResume() {
        super.onResume()
        initLottieView()
    }
}