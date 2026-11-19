package org.sopt.official.webview.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import org.sopt.official.webview.view.WebViewActivity

class OpenWebViewForAppScheme : ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, WebViewActivity::class.java).putExtra(WebViewActivity.INTENT_URL, input)

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) intent?.dataString else null
}
