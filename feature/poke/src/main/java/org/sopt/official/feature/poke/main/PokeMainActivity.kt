package org.sopt.official.feature.poke.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.viewBinding
import org.sopt.official.common.view.toast
import org.sopt.official.domain.poke.entity.PokeFriend
import org.sopt.official.domain.poke.entity.PokeFriendOfFriendList
import org.sopt.official.domain.poke.entity.PokeMe
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.notification.PokeNotificationActivity
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeMainBinding
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment

@AndroidEntryPoint
class PokeMainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityPokeMainBinding::inflate)
    private val viewModel by viewModels<PokeMainViewModel>()

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()
        initListener()
        initStateFlowValues()
    }

    private fun initListener() {
        with(binding) {
            btnClose.setOnClickListener { finish() }
            btnNextSomeonePokeMe.setOnClickListener {
                startActivity(Intent(this@PokeMainActivity, PokeNotificationActivity::class.java))
            }
            btnSomeonePokeMe.setOnClickListener {
                showMessageListBottomSheet()
            }

            scrollviewPokeMain.viewTreeObserver.addOnScrollChangedListener {
                refreshLayoutPokeMain.isEnabled = binding.scrollviewPokeMain.scrollY == 0
            }
            refreshLayoutPokeMain.setOnRefreshListener {
                initData()
                refreshLayoutPokeMain.isRefreshing = false
            }
        }
    }

    private fun showMessageListBottomSheet() {
        if (messageListBottomSheet?.isAdded == true) return
        if (messageListBottomSheet == null) {
            messageListBottomSheet = MessageListBottomSheetFragment.Builder()
//                .onClickMessageListItem { message -> viewModel.pokeUser(userId, message) }
                .create()
        }

        messageListBottomSheet?.let {
            it.show(supportFragmentManager, it.tag)
        }
    }

    private fun initData() {
        viewModel.getPokeMe()
        viewModel.getPokeFriend()
        viewModel.getPokeFriendOfFriend()
    }

    private fun initStateFlowValues() {
        viewModel.apply {
            pokeMeUiState.flowWithLifecycle(lifecycle)
                .onEach {
                    when (it) {
                        is UiState.Loading -> "Loading"
                        is UiState.Success<PokeMe> -> initPokeMeView(it.data)
                        is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                        is UiState.Failure -> it.throwable.message?.let { toast(it) }
                    }
                }
                .launchIn(lifecycleScope)

            pokeFriendUiState.flowWithLifecycle(lifecycle)
                .onEach {
                    when (it) {
                        is UiState.Loading -> "Loading"
//                        is UiState.Success<List<PokeFriend>> -> initPokeFriendView(it.data)
                        is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                        is UiState.Failure -> it.throwable.message?.let { toast(it) }
                        else -> {}
                    }
                }
                .launchIn(lifecycleScope)

            pokeFriendOfFriendUiState.flowWithLifecycle(lifecycle)
                .onEach {
                    when (it) {
                        is UiState.Loading -> "Loading"
                        is UiState.Success<List<PokeFriendOfFriendList>> -> {
                            setPokeFriendOfFriendVisible(it.data)
                            initPokeFriendOfFriendView(it.data)
                        }
                        is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                        is UiState.Failure -> it.throwable.message?.let { toast(it) }
                    }
                }
                .launchIn(lifecycleScope)

            pokeUserUiState.flowWithLifecycle(lifecycle)
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
    }

    private fun initPokeMeView(pokeMeItem: PokeMe) {
        with(binding) {
            pokeMeItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfileSomeonePokeMe.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfileSomeonePokeMe.setImageResource(R.drawable.ic_empty_profile)
            tvUserNameSomeonePokeMe.text = pokeMeItem.name
            tvUserGenerationSomeonePokeMe.text = "${pokeMeItem.generation}기 ${pokeMeItem.part}"
            tvUserMsgSomeonePokeMe.text = pokeMeItem.message
            tvFriendsStatusSomeonePokeMe.text = if (pokeMeItem.isFirstMeet) {
                "${pokeMeItem.mutual.first()} 외 ${pokeMeItem.mutual.size - 1}명과 친구"
            } else {
                "친한친구 ${pokeMeItem.pokeNum}콕"
            }
        }
    }

    private fun setPokeMeViewVisible() {
        binding.layoutSomeonePokeMe.isVisible = false
    }

    private fun initPokeFriendView(pokeFriendItem: PokeFriend) {
        with(binding) {
            pokeFriendItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfilePokeMyFriend.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfilePokeMyFriend.setImageResource(R.drawable.ic_empty_profile)
            tvUserNamePokeMyFriend.text = pokeFriendItem.name
            tvUserGenerationPokeMyFriend.text = "${pokeFriendItem.generation}기 ${pokeFriendItem.part}"
            tvCountPokeMyFriend.text = "${pokeFriendItem.pokeNum}콕"
        }
    }

    private fun setPokeFriendOfFriendVisible(list: List<PokeFriendOfFriendList>) {
        with(binding) {
            fun setVisibility(box1: View, box2: View, emptyBox: View, friendListSize: Int) {
                box1.visibility = if (friendListSize >= 1) View.VISIBLE else View.GONE
                box2.visibility = if (friendListSize == 2) View.VISIBLE else View.GONE
                emptyBox.visibility = if (friendListSize == 0) View.VISIBLE else View.GONE
            }

            when (list.size) {
                1 -> {
                    box2FriendOfFriend.visibility = View.GONE
                    val friendListSize = list[0]?.friendList?.size ?: 0
                    setVisibility(groupFriend1Box1, groupFriend2Box1, groupEmptyBox1, friendListSize)
                }
                2 -> {
                    box2FriendOfFriend.visibility = View.VISIBLE
                    val friendListSize1 = list[0]?.friendList?.size ?: 0
                    val friendListSize2 = list[1]?.friendList?.size ?: 0

                    setVisibility(groupFriend1Box1, groupFriend2Box1, groupEmptyBox1, friendListSize1)
                    setVisibility(groupFriend3Box2, groupFriend4Box2, groupEmptyBox2, friendListSize2)
                }
            }
        }
    }

    private fun initPokeFriendOfFriendView(list: List<PokeFriendOfFriendList>) {
        with(binding) {
            val tvMyFriendNames = listOf(tvMyFriendName1, tvMyFriendName2)
            val friendProfileImageViews = listOf(
                imgFriendProfile1OfMyFriend, imgFriendProfile2OfMyFriend,
                imgFriendProfile3OfMyFriend, imgFriendProfile4OfMyFriend
            )
            val friendTextViews = listOf(
                tvFriendName1OfMyFriend, tvFriendName2OfMyFriend,
                tvFriendName3OfMyFriend, tvFriendName4OfMyFriend
            )
            val friendGenerationTextViews = listOf(
                tvFriendGeneration1OfMyFriend, tvFriendGeneration2OfMyFriend,
                tvFriendGeneration3OfMyFriend, tvFriendGeneration4OfMyFriend
            )

            list.take(2).forEachIndexed { index, response ->
                tvMyFriendNames[index].text = response?.friendName

                (0 until 2).forEach { friendIndex ->
                    val friend = response?.friendList?.getOrNull(friendIndex)
                    val friendProfileImageView = friendProfileImageViews[2 * index + friendIndex]
                    val friendNameTextView = friendTextViews[2 * index + friendIndex]
                    val friendGenerationTextView = friendGenerationTextViews[2 * index + friendIndex]

                    friend?.profileImage?.takeIf { it.isNotEmpty() }?.let {
                        friendProfileImageView.load(it) { transformations(CircleCropTransformation()) }
                    } ?: run {
                        imgUserProfilePokeMyFriend.setImageResource(R.drawable.ic_empty_profile)
                    }
                    friendNameTextView.text = friend?.name
                    friendGenerationTextView.text = "${friend?.generation}기 ${friend?.part}"
                }
            }
        }
    }
}