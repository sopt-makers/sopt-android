package org.sopt.official.plugin

import org.gradle.api.Project
import org.sopt.official.plugin.base.BasePlugin
import org.sopt.official.plugin.configuration.DependencyManager.addKotlinDependencies

class AndroidKotlinPlugin : BasePlugin() {
    override fun apply(target: Project) = with(target) {
        addKotlinDependencies(libs)
    }
}
