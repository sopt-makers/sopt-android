package org.sopt.official.feature.deeplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.deeplink.AppDeeplinkModule
import org.sopt.official.deeplink.AppDeeplinkModuleRegistry
import org.sopt.official.feature.fortune.deeplink.FortuneDeeplinkModule
import org.sopt.official.feature.fortune.deeplink.FortuneDeeplinkModuleRegistry
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.webview.deeplink.WebDeeplinkModule
import org.sopt.official.webview.deeplink.WebDeeplinkModuleRegistry
import javax.inject.Inject

@AndroidEntryPoint
@DeepLinkHandler(value = [AppDeeplinkModule::class, FortuneDeeplinkModule::class, WebDeeplinkModule::class])
class DeepLinkSchemeActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: SoptDataStore

    @Inject
    lateinit var navigator: NavigatorProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (dataStore.accessToken.isEmpty()) {
            startActivity(navigator.getAuthActivityIntent())
            finish()
            return
        }

        DeepLinkDelegate(
            AppDeeplinkModuleRegistry(),
            FortuneDeeplinkModuleRegistry(),
            WebDeeplinkModuleRegistry()
        ).dispatchFrom(this)
        finish()
    }
}
