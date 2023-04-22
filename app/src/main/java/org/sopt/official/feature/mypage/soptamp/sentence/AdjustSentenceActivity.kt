package org.sopt.official.feature.mypage.soptamp.sentence

import android.content.Context
import android.content.Intent
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

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, AdjustSentenceActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}