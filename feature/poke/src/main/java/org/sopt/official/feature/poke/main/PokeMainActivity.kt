package org.sopt.official.feature.poke

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.viewBinding
import org.sopt.official.common.view.toast
import org.sopt.official.domain.poke.entity.PokeFriendOfFriendList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.databinding.ActivityPokeMainBinding
import org.sopt.official.feature.poke.friend_list_summary.FriendListSummaryActivity
import org.sopt.official.feature.poke.main.PokeMainViewModel
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.notification.PokeNotificationActivity
import org.sopt.official.feature.poke.util.setRelationStrokeColor

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

            imgNextPokeMyFriend.setOnClickListener {
                startActivity(Intent(this@PokeMainActivity, FriendListSummaryActivity::class.java))
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
                        // todo: 데이터 없으면 null로 넘어옴.. 처리 필요..
                        is UiState.Success<PokeUser> -> initPokeMeView(it.data)
                        is UiState.ApiError -> startActivity(
                            Intent(
                                this@PokeMainActivity,
                                PokeMainActivity::class.java
                            )
                        ) // if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                        is UiState.Failure -> it.throwable.message?.let { toast(it) }
                    }
                }
                .launchIn(lifecycleScope)
        }

        viewModel.apply {
            pokeFriendUiState.flowWithLifecycle(lifecycle)
                .onEach {
                    when (it) {
                        is UiState.Loading -> "Loading"
                        is UiState.Success<PokeUser> -> initPokeFriendView(it.data)
                        is UiState.ApiError -> startActivity(
                            Intent(
                                this@PokeMainActivity,
                                PokeMainActivity::class.java
                            )
                        ) // if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                        is UiState.Failure -> it.throwable.message?.let { toast(it) }
                    }
                }
                .launchIn(lifecycleScope)
        }

        viewModel.apply {
            pokeFriendOfFriendUiState.flowWithLifecycle(lifecycle)
                .onEach {
                    when (it) {
                        is UiState.Loading -> "Loading"
                        is UiState.Success<List<PokeFriendOfFriendList>> -> {
                            setPokeFriendOfFriendVisible(it.data)
                            initPokeFriendOfFriendView(it.data)
                        }

                        is UiState.ApiError -> startActivity(
                            Intent(
                                this@PokeMainActivity,
                                PokeMainActivity::class.java
                            )
                        ) // if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                        is UiState.Failure -> it.throwable.message?.let { toast(it) }
                    }
                }
                .launchIn(lifecycleScope)
        }

        viewModel.pokeUserUiState
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        viewModel.updatePokeUserState(it.data.userId)
                    }

                    is UiState.ApiError -> {
                        messageListBottomSheet?.dismiss()
                        showAlertToast(it.responseMessage)
                    }

                    is UiState.Failure -> {
                        messageListBottomSheet?.dismiss()
                        it.throwable.message?.let { showAlertToast(it) }
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun initPokeMeView(pokeMeItem: PokeUser) {
        with(binding) {
            imgUserProfileSomeonePokeMe.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, pokeMeItem.playgroundId))))
            }
            pokeMeItem.profileImage.takeIf { !it.isNullOrEmpty() }?.let {
                imgUserProfileSomeonePokeMe.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfileSomeonePokeMe.setImageResource(R.drawable.ic_empty_profile)
            imgUserProfilePokeMeOutline.setRelationStrokeColor(pokeMeItem.relationName)
            tvUserNameSomeonePokeMe.text = pokeMeItem.name
            tvUserGenerationSomeonePokeMe.text = "${pokeMeItem.generation}기 ${pokeMeItem.part}"
            tvUserMsgSomeonePokeMe.text = pokeMeItem.message
            tvFriendsStatusSomeonePokeMe.text = if (pokeMeItem.isFirstMeet) {
                "${pokeMeItem.mutual.first()} 외 ${pokeMeItem.mutual.size - 1}명과 친구"
            } else {
                "${pokeMeItem.relationName} ${pokeMeItem.pokeNum}콕"
            }
            btnSomeonePokeMe.isEnabled = !pokeMeItem.isAlreadyPoke
            btnSomeonePokeMe.setOnClickListener {
                showMessageListBottomSheet(pokeMeItem.userId, pokeMeItem.isFirstMeet)
            }
        }
    }

    private fun initPokeFriendView(pokeFriendItem: PokeUser) {
        with(binding) {
            imgUserProfilePokeMyFriend.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, pokeFriendItem.playgroundId))))
            }
            pokeFriendItem.profileImage.takeIf { !it.isNullOrEmpty() }?.let {
                imgUserProfilePokeMyFriend.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfilePokeMyFriend.setImageResource(R.drawable.ic_empty_profile)
            imgUserProfilePokeMyFriendOutline.setRelationStrokeColor(pokeFriendItem.relationName)
            tvUserNamePokeMyFriend.text = pokeFriendItem.name
            tvUserGenerationPokeMyFriend.text = "${pokeFriendItem.generation}기 ${pokeFriendItem.part}"
            tvCountPokeMyFriend.text = "${pokeFriendItem.pokeNum}콕"
            btnPokeMyFriend.isEnabled = !pokeFriendItem.isAlreadyPoke
            btnPokeMyFriend.setOnClickListener {
                showMessageListBottomSheet(pokeFriendItem.userId, pokeFriendItem.isFirstMeet)
            }
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
                    val friendListSize = list[0].friendList.size
                    setVisibility(groupFriend1Box1, groupFriend2Box1, groupEmptyBox1, friendListSize)
                }

                2 -> {
                    box2FriendOfFriend.visibility = View.VISIBLE
                    val friendListSize1 = list[0].friendList.size
                    val friendListSize2 = list[1].friendList.size

                    setVisibility(groupFriend1Box1, groupFriend2Box1, groupEmptyBox1, friendListSize1)
                    setVisibility(groupFriend3Box2, groupFriend4Box2, groupEmptyBox2, friendListSize2)
                }
            }
        }
    }

    private fun initPokeFriendOfFriendView(list: List<PokeFriendOfFriendList>) {
        with(binding) {
            val myFriendNameTextViews = listOf(tvMyFriendName1, tvMyFriendName2)
            val myFriendProfileImageViews = listOf(imgMyFriendProfile1, imgMyFriendProfile2)

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
            val btnPokeImageViews = listOf(
                btnFriendPoke1OfMyFriend, btnFriendPoke2OfMyFriend,
                btnFriendPoke3OfMyFriend, btnFriendPoke4OfMyFriend
            )

            list.take(2).forEachIndexed { index, friend ->
                myFriendNameTextViews[index].text = friend.friendName
                myFriendProfileImageViews[index].setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, friend.playgroundId))))
                }
                friend.friendProfileImage.takeIf { it.isNotEmpty() }?.let {
                    myFriendProfileImageViews[index].load(it) { transformations(CircleCropTransformation()) }
                } ?: run {
                    myFriendProfileImageViews[index].setImageResource(R.drawable.ic_empty_profile)
                }

                (0 until 2).forEach { friendIndex ->
                    val friendOfFriend = friend.friendList.getOrNull(friendIndex)
                    val friendProfileImageView = friendProfileImageViews[2 * index + friendIndex]
                    val friendNameTextView = friendTextViews[2 * index + friendIndex]
                    val friendGenerationTextView = friendGenerationTextViews[2 * index + friendIndex]
                    val btnPokeImageView = btnPokeImageViews[2 * index + friendIndex]

                    friendOfFriend?.let { myFriendOfFriend ->
                        friendProfileImageView.setOnClickListener {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(getString(R.string.poke_user_profile_url, myFriendOfFriend.playgroundId))
                                )
                            )
                        }
                        myFriendOfFriend.profileImage.takeIf { !it.isNullOrEmpty() }?.let { url ->
                            friendProfileImageView.load(url) { transformations(CircleCropTransformation()) }
                        } ?: run {
                            friendProfileImageView.setImageResource(R.drawable.ic_empty_profile)
                        }
                        friendNameTextView.text = myFriendOfFriend.name
                        friendGenerationTextView.text = "${myFriendOfFriend.generation}기 ${myFriendOfFriend.part}"
                        btnPokeImageView.isEnabled = !myFriendOfFriend.isAlreadyPoke
                        btnPokeImageView.setOnClickListener {
                            showMessageListBottomSheet(myFriendOfFriend.userId, myFriendOfFriend.isFirstMeet)
                        }
                    }
                }
            }
        }
    }

    private fun showMessageListBottomSheet(userId: Int, isFirstMeet: Boolean) {
        val messageType = when (isFirstMeet) {
            true -> PokeMessageType.REPLY_NEW
            false -> PokeMessageType.POKE_FRIEND
        }
        if (messageListBottomSheet?.isAdded == true) return
        if (messageListBottomSheet == null) {
            messageListBottomSheet = MessageListBottomSheetFragment.Builder()
                .setMessageListType(messageType)
                .onClickMessageListItem { message -> viewModel.pokeUser(userId, message) }
                .create()
        }

        messageListBottomSheet?.let {
            it.show(supportFragmentManager, it.tag)
        }
    }
}