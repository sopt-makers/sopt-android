package org.sopt.official.feature.poke

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
import org.sopt.official.R
import org.sopt.official.common.util.viewBinding
import org.sopt.official.data.model.poke.response.PokeFriendOfFriendResponse
import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse
import org.sopt.official.databinding.ActivityPokeMainBinding
import org.sopt.official.util.PokeUtil

@AndroidEntryPoint
class PokeMainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityPokeMainBinding::inflate)
    private val viewModel by viewModels<PokeMainViewModel>()
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
                val bottomSheetDialog = PokeMessageBottomSheetDialogFragment()
                bottomSheetDialog.show(supportFragmentManager, "PokeMessageBottomSheetDialogFragment")
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
            pokeMe.flowWithLifecycle(lifecycle)
                .onEach {
                    if (it == null) {
                        setPokeMeViewVisible(false)
                    } else {
                        setPokeMeViewVisible(true)
                        initPokeMeView(it)
                    }
                }
                .launchIn(lifecycleScope)
        }

        viewModel.apply {
            pokeFriend.flowWithLifecycle(lifecycle)
                .onEach {
                    it?.let { initPokeFriendView(it) }
                }
                .launchIn(lifecycleScope)
        }

        viewModel.apply {
            pokeFriendOfFriend.flowWithLifecycle(lifecycle)
                .onEach {
                    if (it.isNotEmpty()) {
                        setPokeFriendOfFriendVisible(it)
                        initPokeFriendOfFriendView(it)
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun initPokeMeView(pokeMeItem: PokeMeResponse) {
        with(binding) {
            pokeMeItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfileSomeonePokeMe.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfileSomeonePokeMe.setImageResource(R.drawable.ic_empty_profile)
            imgUserRelationSomeonePokeMe.setImageResource(PokeUtil.convertRelationNameToBorderReSourceId(pokeMeItem.relationName))
            tvUserNameSomeonePokeMe.text = pokeMeItem.name
            tvUserGenerationSomeonePokeMe.text = "${pokeMeItem.generation}기 ${pokeMeItem.part}"
            tvUserMsgSomeonePokeMe.text = pokeMeItem.message
            tvFriendsStatusSomeonePokeMe.text = if (pokeMeItem.isFirstMeet) {
                "${pokeMeItem.mutual.first()} 외 ${pokeMeItem.mutual.size - 1}명과 친구"
            } else {
                "${pokeMeItem.relationName} ${pokeMeItem.pokeNum}콕"
            }
            btnSomeonePokeMe.setImageResource(PokeUtil.setPokeIcon(pokeMeItem.isFirstMeet, pokeMeItem.isAlreadyPoke))
        }
    }

    private fun setPokeMeViewVisible(isVisible: Boolean) {
        binding.layoutSomeonePokeMe.isVisible = isVisible
    }

    private fun initPokeFriendView(pokeFriendItem: PokeFriendResponse) {
        with(binding) {
            pokeFriendItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfilePokeMyFriend.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfilePokeMyFriend.setImageResource(R.drawable.ic_empty_profile)
            imgUserRelationPokeMyFriend.setImageResource(PokeUtil.convertRelationNameToBorderReSourceId(pokeFriendItem.relationName))
            tvUserNamePokeMyFriend.text = pokeFriendItem.name
            tvUserGenerationPokeMyFriend.text = "${pokeFriendItem.generation}기 ${pokeFriendItem.part}"
            tvCountPokeMyFriend.text = "${pokeFriendItem.pokeNum}콕"
            btnPokeMyFriend.setImageResource(PokeUtil.setPokeIcon(pokeFriendItem.isFirstMeet, pokeFriendItem.isAlreadyPoke))
        }
    }

    private fun setPokeFriendOfFriendVisible(list: List<PokeFriendOfFriendResponse?>) {
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

    private fun initPokeFriendOfFriendView(list: List<PokeFriendOfFriendResponse?>) {
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

            list.take(2).forEachIndexed { index, response ->
                myFriendNameTextViews[index].text = response?.friendName
                response?.friendProfileImage?.takeIf { it.isNotEmpty() }?.let {
                    myFriendProfileImageViews[index].load(it) { transformations(CircleCropTransformation()) }
                } ?: run {
                    myFriendProfileImageViews[index].setImageResource(R.drawable.ic_empty_profile)
                }

                (0 until 2).forEach { friendIndex ->
                    val friend = response?.friendList?.getOrNull(friendIndex)
                    val friendProfileImageView = friendProfileImageViews[2 * index + friendIndex]
                    val friendNameTextView = friendTextViews[2 * index + friendIndex]
                    val friendGenerationTextView = friendGenerationTextViews[2 * index + friendIndex]
                    val btnPokeImageView = btnPokeImageViews[2 * index + friendIndex]

                    friend?.let {
                        it.profileImage.takeIf { url -> url.isNotEmpty() }?.let { url ->
                            friendProfileImageView.load(url) { transformations(CircleCropTransformation()) }
                        } ?: run {
                            friendProfileImageView.setImageResource(R.drawable.ic_empty_profile)
                        }
                        friendNameTextView.text = it.name
                        friendGenerationTextView.text = "${it.generation}기 ${it.part}"
                        btnPokeImageView.setImageResource(PokeUtil.setPokeIcon(it.isFirstMeet, it.isAlreadyPoke))
                    }
                }
            }
        }
    }
}