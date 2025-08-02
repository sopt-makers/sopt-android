package org.sopt.official.plugin

import org.gradle.api.Project
import org.sopt.official.plugin.base.BasePlugin
import org.sopt.official.plugin.configuration.DependencyManager.addRetrofitDependencies

class RetrofitPlugin : BasePlugin() {
    override fun apply(target: Project) = with(target) {
        addRetrofitDependencies(libs)
    }
}
