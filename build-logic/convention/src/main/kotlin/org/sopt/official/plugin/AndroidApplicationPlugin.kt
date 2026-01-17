package org.sopt.official.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.sopt.official.plugin.base.AndroidBasePlugin
import org.sopt.official.plugin.configuration.ConfigurationManager
import org.sopt.official.plugin.configuration.ConfigurationManager.configureBuildConfigFields
import org.sopt.official.plugin.configuration.DependencyManager.addCoreAndroidDependencies
import org.sopt.official.plugin.configuration.DependencyManager.addMetroAndroidDependencies
import org.sopt.official.plugin.configuration.DependencyManager.addMetroViewModelDependencies

class AndroidApplicationPlugin : AndroidBasePlugin() {
    override fun apply(target: Project) = with(target) {
        applyPlugin("com.android.application")
        applyPlugin("kotlin-parcelize")

        apply<AndroidKotlinPlugin>()
        apply<KotlinSerializationPlugin>()
        apply<RetrofitPlugin>()

        apply<AndroidMetroPlugin>()
        addMetroAndroidDependencies(libs)
        addMetroViewModelDependencies(libs)

        configureAndroidBase()

        val properties = ConfigurationManager.loadProperties(this)
        extensions.getByType<BaseExtension>().apply {
            configureBuildConfigFields(properties)
        }

        addCoreAndroidDependencies(libs)
    }
}