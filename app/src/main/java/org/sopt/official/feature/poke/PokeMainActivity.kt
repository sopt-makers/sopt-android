package org.sopt.official.feature.poke

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

@AndroidEntryPoint
class PokeMainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityPokeMainBinding::inflate)
    private val viewModel by viewModels<PokeMainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.scrollviewPokeMain.viewTreeObserver.addOnScrollChangedListener {
            binding.refreshLayoutPokeMain.isEnabled = binding.scrollviewPokeMain.scrollY == 0
        }

        initClickListener()
        initViewModel()
        initStateFlowValues()
    }

    private fun initClickListener() {
        with(binding) {
            btnClose.setOnClickListener { finish() }
            btnNextSomeonePokeMe.setOnClickListener {
                startActivity(Intent(this@PokeMainActivity, PokeNotificationActivity::class.java))
            }
        }
    }

    private fun initViewModel() {
        viewModel.getPokeMe()
        viewModel.getPokeFriend()
        viewModel.getPokeFriendOfFriend()
    }

    private fun initStateFlowValues() {
        viewModel.apply {
            pokeMe.flowWithLifecycle(lifecycle)
                .onEach {
                    it?.let { initPokeMeView(it) }
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

    private fun initPokeFriendView(pokeFriendItem: PokeFriendResponse) {
        with(binding) {
            pokeFriendItem.profileImage.takeIf { it.isNotEmpty() }?.let {
                imgUserProfilePokeMyFriend.load(it) { transformations(CircleCropTransformation()) }
            } ?: imgUserProfilePokeMyFriend.setImageResource(R.drawable.ic_empty_profile)
            tvUserNamePokeMyFriend.text = pokeFriendItem.name
            tvUserGenerationPokeMyFriend.text = "${pokeFriendItem.generation}기 ${pokeFriendItem.part}"
            tvCountPokeMyFriend.text = "${pokeFriendItem.pokeNum}콕"
        }
    }

    private fun setPokeFriendOfFriendVisible(list: List<PokeFriendOfFriendResponse?>) {
        with(binding) {
            box2FriendOfFriend.isVisible = list.size == 2
            groupEmptyBox1.isVisible = list.first()?.friendList!!.isEmpty()
        }
    }
    private fun initPokeFriendOfFriendView(list: List<PokeFriendOfFriendResponse?>) {
//        val friend1 = list[0]?.friendList?.get(0)
//        val friend2 = list[0]?.friendList?.get(1)
//        val friend3 = list[1]?.friendList?.get(0)
//        val friend4 = list[1]?.friendList?.get(1)
//
//        with(binding) {
//            tvMyFriendName1.text = list[0]?.friendName
//            tvFriendName1OfMyFriend.text = friend1?.name
//            tvFriendGeneration1OfMyFriend.text = "${friend1?.generation}기 ${friend1?.part}"
//            tvFriendName2OfMyFriend.text = friend2?.name
//            tvFriendGeneration2OfMyFriend.text = "${friend2?.generation}기 ${friend2?.part}"
//
//            tvMyFriendName2.text = list[1]?.friendName
//            tvFriendName3OfMyFriend.text = friend3?.name
//            tvFriendGeneration3OfMyFriend.text = "${friend3?.generation}기 ${friend3?.part}"
//            tvFriendName4OfMyFriend.text = friend4?.name
//            tvFriendGeneration4OfMyFriend.text = "${friend4?.generation}기 ${friend4?.part}"
//        }
    }
}