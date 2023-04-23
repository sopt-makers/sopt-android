package org.sopt.official.feature.mypage.soptamp.sentence

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
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
        initClick()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initClick() {
        binding.confirmButton.setOnClickListener {
            viewModel.adjustSentence()
            this.finish()
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, AdjustSentenceActivity::class.java).apply {
        }
    }
}