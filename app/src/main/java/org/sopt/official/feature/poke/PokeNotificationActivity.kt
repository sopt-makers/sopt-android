package org.sopt.official.feature.poke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.common.util.viewBinding
import org.sopt.official.databinding.ActivityPokeNotificationBinding

@AndroidEntryPoint
class PokeNotificationActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityPokeNotificationBinding::inflate)
    private val viewModel by viewModels<PokeNotificationViewModel>()

    private val pokeNotificationAdapter
        get() = binding.recyclerviewPokeNotification.adapter as PokeNotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_notification)

        initStateFlowValues()
    }

    private fun initStateFlowValues() {
        lifecycleScope.launch {
            viewModel.pokeNotification.collectLatest {
                if (it != null) {
                    pokeNotificationAdapter.updatePokeNotification(it.history)
                }
            }
        }
    }
}