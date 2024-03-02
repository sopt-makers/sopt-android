import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

fun PluginDependenciesSpec.sopt(pluginName: String): PluginDependencySpec {
    return id("org.sopt.official.$pluginName")
}
