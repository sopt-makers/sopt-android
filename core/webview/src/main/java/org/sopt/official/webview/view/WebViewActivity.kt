package org.sopt.official.webview.view

import android.os.Bundle
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

        val linkUrl = intent.getStringExtra(INTENT_URL)
        linkUrl?.let { binding.webView.loadUrl(it) }
    }
}
