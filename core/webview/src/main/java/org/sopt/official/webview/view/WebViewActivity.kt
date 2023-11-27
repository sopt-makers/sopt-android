package org.sopt.official.webview.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.webview.databinding.ActivityWebViewBinding
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    companion object {
        const val INTENT_URL = "_intent_url"
    }

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mLinkUrl = intent.getStringExtra(INTENT_URL)
        mLinkUrl?.let { binding.webView.loadUrl(it) }
    }
}