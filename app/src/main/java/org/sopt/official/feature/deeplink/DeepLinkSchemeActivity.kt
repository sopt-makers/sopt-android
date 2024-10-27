package org.sopt.official.feature.deeplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import org.sopt.official.deeplink.AppDeeplinkModule
import org.sopt.official.deeplink.AppDeeplinkModuleRegistry
import org.sopt.official.feature.fortune.deeplink.FortuneDeeplinkModule
import org.sopt.official.feature.fortune.deeplink.FortuneDeeplinkModuleRegistry

@DeepLinkHandler(value = [AppDeeplinkModule::class, FortuneDeeplinkModule::class])
class DeepLinkSchemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // val deeplinkDelegate
        DeepLinkDelegate(AppDeeplinkModuleRegistry(), FortuneDeeplinkModuleRegistry()).dispatchFrom(this)
        finish()
    }
}
