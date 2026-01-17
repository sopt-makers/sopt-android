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
package org.sopt.official.feature.poke.friend.summary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import dev.zacsweers.metro.Inject
import java.io.Serializable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.model.UserStatus
import org.sopt.official.common.util.colorOf
import org.sopt.official.common.util.dp
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.common.util.viewBinding
import org.sopt.official.common.di.SoptViewModelFactory
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityFriendListSummaryBinding
import org.sopt.official.feature.poke.friend.detail.FriendListDetailBottomSheetFragment
import org.sopt.official.feature.poke.main.PokeMainActivity
import org.sopt.official.feature.poke.message.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.user.ItemDecorationDivider
import org.sopt.official.feature.poke.user.PokeUserListAdapter
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.user.PokeUserListItemViewType
import org.sopt.official.feature.poke.util.addOnAnimationEndListener
import org.sopt.official.feature.poke.util.setRelationStrokeColor
import org.sopt.official.feature.poke.util.showPokeToast

@Deprecated("FriendListSummaryScreen으로 대체")
class FriendListSummaryActivity(
    private val viewModelFactory: SoptViewModelFactory,
    private val tracker: Tracker
) : AppCompatActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    private val binding by viewBinding(ActivityFriendListSummaryBinding::inflate)
    private val viewModel by viewModels<FriendListSummaryViewModel>()

    private val args by serializableExtra(StartArgs(""))
    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

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

        enableEdgeToEdge()
        initAppBar()
        initLottieListener()
        initFriendListBlock()
        launchFriendListSummaryUiStateFlow()
        launchPokeUserUiStateFlow()
        handleDeepLinkInitialType()
    }

    override fun onResume() {
        super.onResume()
        tracker.track(type = EventType.VIEW, name = "poke_friend", properties = mapOf("view_type" to args?.userStatus))
        initLottieView()
    }

    private fun initAppBar() {
        binding.includeAppBar.apply {
            toolbar.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            textViewTitle.text = getString(R.string.my_friend_title)
        }
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
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            FriendListDetailBottomSheetFragment::class.java.classLoader!!,
            FriendListDetailBottomSheetFragment::class.java.name
        ) as FriendListDetailBottomSheetFragment

        fragment.arguments = android.os.Bundle().apply {
            putString("userStatus", args?.userStatus ?: UserStatus.UNAUTHENTICATED.name)
            putSerializable("pokeFriendType", pokeFriendType)
        }

        fragment.show(supportFragmentManager, fragment.tag)
    }

    private fun launchFriendListSummaryUiStateFlow() {
        viewModel.friendListSummaryUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
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
            override fun onClickProfileImage(userId: Int) {
                tracker.track(
                    type = EventType.CLICK,
                    name = "memberprofile",
                    properties =
                        mapOf(
                            "view_type" to args?.userStatus,
                            "click_view_type" to "friend",
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
                            "view_type" to args?.userStatus,
                            "click_view_type" to "friend",
                            "view_profile" to user.userId,
                        ),
                )
                
                val bottomSheet = supportFragmentManager.fragmentFactory.instantiate(
                    MessageListBottomSheetFragment::class.java.classLoader!!,
                    MessageListBottomSheetFragment::class.java.name
                ) as MessageListBottomSheetFragment

                bottomSheet.arguments = android.os.Bundle().apply {
                    putSerializable("pokeMessageType", PokeMessageType.POKE_FRIEND)
                }

                bottomSheet.onClickMessageListItem = { message, isAnonymous ->
                    viewModel.pokeUser(
                        userId = user.userId,
                        isAnonymous = isAnonymous,
                        message = message
                    )
                }

                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }
        }

    private fun launchPokeUserUiStateFlow() {
        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        viewModel.getFriendListSummary()

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

    private fun initLottieView() {
        with(binding) {
            layoutAnonymousFriendOpen.visibility = View.GONE
            layoutAnonymousFriendLottie.visibility = View.GONE
        }
    }

    // 솝트로그에서 딥링크로 들어왔을 때 바텀시트 띄우는 함수
    private fun handleDeepLinkInitialType() {
        val typeName = args?.initialFriendType

        if (typeName.isNullOrEmpty()) return

        val initialType = PokeFriendType.entries.find {
            it.typeName.equals(typeName, ignoreCase = true)
        } ?: return

        showFriendListDetailBottomSheet(initialType)
    }

    data class StartArgs(
        val userStatus: String,
        val initialFriendType: String? = null // 솝트로그에서 딥링크로 넘어왔을 때 띄울 바텀시트 타입
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, FriendListSummaryActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}