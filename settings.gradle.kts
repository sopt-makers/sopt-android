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
    ":domain:soptamp",
    ":data:soptamp",
    ":core:network",
    ":core:analytics",
    ":core:common",
    ":feature:auth",
    ":feature:soptamp",
    ":domain:mypage"
)
