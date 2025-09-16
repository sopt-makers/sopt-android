package org.sopt.official.plugin.base

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

abstract class BasePlugin : Plugin<Project> {

    protected val Project.libs: VersionCatalog
        get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

    protected fun Project.applyPlugin(pluginId: String) {
        plugins.apply(pluginId)
    }

    abstract override fun apply(target: Project)
}
