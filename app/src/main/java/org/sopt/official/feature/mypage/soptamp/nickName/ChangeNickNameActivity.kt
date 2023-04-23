package org.sopt.official.feature.mypage.soptamp.nickName

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.databinding.ActivityChangeNickNameBinding
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class ChangeNickNameActivity : AppCompatActivity()  {
    private val binding by viewBinding(ActivityChangeNickNameBinding::inflate)
    private val viewModel by viewModels<ChangeNickNameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initView()
        initAction()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initView() {
//        binding.errorMessage.setVisible() -> validate 결과에 따라
//        binding.edittext.backgroundTintList = -> validate 결과에 따라 컬러 변경
    }

    private fun initAction() {
        binding.confirmButton.setOnClickListener {
            viewModel.changeNickName()
            this.finish()
        }
        binding.edittext.addTextChangedListener {
            //api 콜
        }

    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, ChangeNickNameActivity::class.java).apply {
        }
    }
}