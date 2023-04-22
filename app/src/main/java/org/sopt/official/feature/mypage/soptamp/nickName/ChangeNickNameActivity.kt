package org.sopt.official.feature.mypage.soptamp.nickName

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.databinding.ActivityChangeNickNameBinding
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class ChangeNickNameActivity : AppCompatActivity()  {
    private val binding by viewBinding(ActivityChangeNickNameBinding::inflate)
    private val viewModel by viewModels<ChangeNickNameViewModel>()

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, ChangeNickNameActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}