package org.sopt.official.webview.view

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.common.util.viewBinding
import org.sopt.official.webview.databinding.ActivityWebViewBinding

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    companion object {
        const val INTENT_URL = "_intent_url"
    }

    private val binding by viewBinding(ActivityWebViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        handleLinkUrl()
        handleOnBackPressed()
    }

    private fun handleLinkUrl() {
        val linkUrl = intent.getStringExtra(INTENT_URL)
        linkUrl?.let { binding.webView.loadUrl(it) }
    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(owner = this) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                if (!isFinishing) finish()
            }
        }
    }
}
