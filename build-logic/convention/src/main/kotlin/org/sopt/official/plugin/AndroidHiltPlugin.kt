package org.sopt.official.plugin

import org.gradle.api.Project
import org.sopt.official.plugin.base.BasePlugin
import org.sopt.official.plugin.configuration.DependencyManager.addHiltDependencies

class AndroidHiltPlugin : BasePlugin() {
    override fun apply(target: Project) = with(target) {
        applyPlugin("com.google.devtools.ksp")
        applyPlugin("com.google.dagger.hilt.android")

        addHiltDependencies(libs)
    }
}
