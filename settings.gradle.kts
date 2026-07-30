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
    ":core:cache",
    ":core:common",
    ":core:designsystem",
    ":core:network",
    ":core:security",
    ":core:webview",
    ":core:navigation",
    ":core:model",
    ":core:localstorage",

    ":data:auth",
    ":data:home",
    ":data:user",
    ":data:mypage",
    ":data:notification",
    ":data:poke",
    ":data:schedule",
    ":data:soptamp",
    ":data:soptlog",
    ":data:appjamtamp",
    ":data:sopletter",

    ":domain:auth",
    ":domain:home",
    ":domain:user",
    ":domain:mypage",
    ":domain:notification",
    ":domain:poke",
    ":domain:schedule",
    ":domain:soptamp",
    ":domain:soptlog",
    ":domain:appjamtamp",
    ":domain:sopletter",

    ":feature:auth",
    ":feature:home",
    ":feature:mypage",
    ":feature:notification",
    ":feature:poke",
    ":feature:schedule",
    ":feature:soptamp",
    ":feature:soptlog",
    ":feature:main",
    ":feature:appjamtamp",
    ":feature:sopletter"
)
include(":feature:poke-v2")
