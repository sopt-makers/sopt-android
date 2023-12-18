package org.sopt.official.feature.poke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.viewBinding
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

        initViewModel()
        initStateFlowValues()
    }

    private fun initViewModel() {
        viewModel.getPokeMe()
        viewModel.getPokeFriend()
    }

    private fun initStateFlowValues() {
        viewModel.apply {
            pokeMe.flowWithLifecycle(lifecycle)
                .onEach {
                    if (it == null) {
                        setPokeMeViewUnVisible()
                        return@onEach
                    }
                    initPokeMeView(it)
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
    }

    private fun initPokeMeView(pokeMeItem: PokeMeResponse) {
        with(binding) {
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

    private fun setPokeMeViewUnVisible() {
        binding.layoutSomeonePokeMe.isVisible = false
    }

    private fun initPokeFriendView(pokeFriendItem: PokeFriendResponse) {
        with(binding) {
            tvUserNamePokeMyFriend.text = pokeFriendItem.name
            tvUserGenerationPokeMyFriend.text = "${pokeFriendItem.generation}기 ${pokeFriendItem.part}"
            tvCountPokeMyFriend.text = "${pokeFriendItem.pokeNum}콕"
        }
    }
}