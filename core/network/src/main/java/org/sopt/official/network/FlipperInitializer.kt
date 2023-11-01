package org.sopt.official.network

//import android.app.Application
//import com.facebook.flipper.android.AndroidFlipperClient
//import com.facebook.flipper.plugins.inspector.DescriptorMapping
//import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
//import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
//import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
//import com.facebook.soloader.SoLoader
//import okhttp3.OkHttpClient
//
//object FlipperInitializer {
//    private val flipperNetworkPlugin = NetworkFlipperPlugin()
//
//    fun init(app: Application) {
//        SoLoader.init(app, false)
//        val client = AndroidFlipperClient.getInstance(app)
//        client.addPlugin(InspectorFlipperPlugin(app, DescriptorMapping.withDefaults()))
//        client.addPlugin(flipperNetworkPlugin)
//        client.start()
//    }
//
//    fun addFlipperNetworkPlugin(builder: OkHttpClient.Builder): OkHttpClient.Builder {
//        return builder.addNetworkInterceptor(FlipperOkhttpInterceptor(flipperNetworkPlugin))
//    }
//}