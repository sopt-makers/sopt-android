pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://jitpack.io") }
    }
}
rootProject.name = "SOPT"
include(
    ":app",
    ":core:network",
    ":core:analytics",
    ":core:auth",
    ":core:authimpl",
    ":core:common",
    ":core:designsystem",
    ":core:webview",
    ":data:mypage",
    ":data:soptamp",
    ":domain:mypage",
    ":domain:soptamp",
    ":feature:auth",
    ":feature:mypage",
    ":feature:soptamp",
)
