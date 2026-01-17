package org.sopt.official.plugin

import org.gradle.api.Project
import org.sopt.official.plugin.base.BasePlugin
import org.sopt.official.plugin.configuration.DependencyManager.addMetroDependencies

class AndroidMetroPlugin : BasePlugin() {
    override fun apply(target: Project) = with(target) {
        applyPlugin("dev.zacsweers.metro")

        addMetroDependencies(libs)
    }
}
