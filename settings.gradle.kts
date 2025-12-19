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
        maven { setUrl("https://maven.mozilla.org/maven2") }
    }
}
rootProject.name = "SOPT"
include(
    ":app",

    ":baselineprofile",

    ":core:analytics",
    ":core:auth",
    ":core:authimpl",
    ":core:common",
    ":core:designsystem",
    ":core:network",
    ":core:security",
    ":core:webview",
    ":core:navigation",
    ":core:model",

    ":data:auth",
    ":data:fortune",
    ":data:home",
    ":data:mypage",
    ":data:notification",
    ":data:poke",
    ":data:schedule",
    ":data:soptamp",
    ":data:soptlog",
    ":data:appjamtamp",

    ":domain:auth",
    ":domain:fortune",
    ":domain:home",
    ":domain:mypage",
    ":domain:notification",
    ":domain:poke",
    ":domain:schedule",
    ":domain:soptamp",
    ":domain:soptlog",
    ":domain:appjamtamp",

    ":feature:auth",
    ":feature:fortune",
    ":feature:home",
    ":feature:mypage",
    ":feature:notification",
    ":feature:poke",
    ":feature:schedule",
    ":feature:soptamp",
    ":feature:soptlog",
    ":feature:main",
    ":feature:appjamtamp"
)
