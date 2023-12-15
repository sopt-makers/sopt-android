package org.sopt.official.feature.poke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.viewBinding
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
    }

    private fun initStateFlowValues() {
        viewModel.apply {
            pokeMe.flowWithLifecycle(lifecycle)
                .onEach { it?.let { pokeMeItem -> initPokeMeView(pokeMeItem) } }
                .launchIn(lifecycleScope)

        }
    }

    private fun initPokeMeView(pokeMeItem: PokeMeResponse) {
        with(binding) {
            tvUserNameSomeonePokeMe.text = pokeMeItem.name
            tvUserGenerationSomeonePokeMe.text = "${pokeMeItem.activities.first().part} ${pokeMeItem.activities.first().generation}기"
            tvUserMsgSomeonePokeMe.text = pokeMeItem.message
            if (!pokeMeItem.isFirstMeet) { // 이미 친한 친구라면
                tvFriendsStatusSomeonePokeMe.text = "친한친구 ${pokeMeItem.pockNum}콕"
            } else {
                tvFriendsStatusSomeonePokeMe.text = "${pokeMeItem.mutual.first()} 외 ${pokeMeItem.mutual.size - 1}명과 친구"
            }
        }
    }
}