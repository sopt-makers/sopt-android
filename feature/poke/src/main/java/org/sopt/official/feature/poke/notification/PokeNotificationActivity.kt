package org.sopt.official.feature.poke.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.sopt.official.common.util.viewBinding
import org.sopt.official.feature.poke.databinding.ActivityPokeNotificationBinding

@AndroidEntryPoint
class PokeNotificationActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityPokeNotificationBinding::inflate)
    private val viewModel by viewModels<PokeNotificationViewModel>()

    private val pokeNotificationAdapter
        get() = binding.recyclerviewPokeNotification.adapter as PokeNotificationAdapter

    private val pokeNotificationLayoutManager
        get() = binding.recyclerviewPokeNotification.layoutManager as LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        initRecyclerView()
        initViewModel()
        initStateFlowValues()
    }

    private fun initRecyclerView() {
        with(binding.recyclerviewPokeNotification) {
            adapter = PokeNotificationAdapter()
            addOnScrollListener(scrollListener)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisibleItemPosition = pokeNotificationLayoutManager.findLastVisibleItemPosition()
            val totalItemCount = pokeNotificationLayoutManager.itemCount
            if (lastVisibleItemPosition == totalItemCount - 1 && totalItemCount % 10 == 0) {
                viewModel.getPokeNotification()
            }
        }
    }

    private fun initViewModel() {
        viewModel.getPokeNotification()
    }

    private fun initStateFlowValues() {
        lifecycleScope.launch {
            viewModel.pokeNotification.collectLatest {
                pokeNotificationAdapter.updatePokeNotification(it)
            }
        }
    }
}