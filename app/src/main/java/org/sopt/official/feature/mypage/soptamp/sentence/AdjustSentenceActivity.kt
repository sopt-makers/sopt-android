package org.sopt.official.feature.mypage.soptamp.sentence

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.databinding.ActivityAdjustSentenceBinding
import org.sopt.official.feature.mypage.MyPageActivity
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class AdjustSentenceActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAdjustSentenceBinding::inflate)
    private val viewModel by viewModels<AdjustSentenceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initView()
        initClick()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initView() {
        viewModel.backPressedSignal
            .flowWithLifecycle(lifecycle)
            .filter { it }
            .onEach {
                this.onBackPressedDispatcher.onBackPressed()
            }
            .launchIn(lifecycleScope)
        binding.edittext.addTextChangedListener {
            binding.confirmButton.isEnabled = (it?.length ?: 0) > 0
        }
    }

    private fun initClick() {
        binding.confirmButton.setOnClickListener {
            val text = binding.edittext.text.toString()
            viewModel.adjustSentence(text)
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, AdjustSentenceActivity::class.java).apply {
        }
    }
}