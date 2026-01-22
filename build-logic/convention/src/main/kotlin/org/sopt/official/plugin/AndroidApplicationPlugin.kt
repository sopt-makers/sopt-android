package org.sopt.official.plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.sopt.official.plugin.base.AndroidBasePlugin
import org.sopt.official.plugin.configuration.ConfigurationManager
import org.sopt.official.plugin.configuration.ConfigurationManager.configureBuildConfigFields
import org.sopt.official.plugin.configuration.DependencyManager.addCoreAndroidDependencies

class AndroidApplicationPlugin : AndroidBasePlugin() {
    override fun apply(target: Project) = with(target) {
        applyPlugin("com.android.application")
        applyPlugin("kotlin-parcelize")

        apply<AndroidKotlinPlugin>()
        apply<KotlinSerializationPlugin>()
        apply<RetrofitPlugin>()

        apply<AndroidHiltPlugin>()

        configureAndroidBase()

        val properties = ConfigurationManager.loadProperties(this)
        extensions.getByType<CommonExtension>().apply {
            configureBuildConfigFields(properties)
        }

        addCoreAndroidDependencies(libs)
    }
}
