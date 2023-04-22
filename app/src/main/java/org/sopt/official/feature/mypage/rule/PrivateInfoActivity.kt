package org.sopt.official.feature.mypage.rule

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.databinding.ActivityPrivateInfoBinding
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class PrivateInfoActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPrivateInfoBinding::inflate)

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, PrivateInfoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}