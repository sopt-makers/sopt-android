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
    ":domain:mypage",
    ":core:network",
    ":core:analytics",
    ":core:common",
    ":data:soptamp",
    ":data:mypage",
    ":feature:auth",
    ":feature:soptamp",
    ":feature:mypage",
)
include(":core:auth")
include(":core:authimpl")
